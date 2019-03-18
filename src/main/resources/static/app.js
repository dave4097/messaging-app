var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#messages").html("");
    $.get("/messaging", function(messagesWrapper, status) {
        var messageArray = messagesWrapper.messages;
        for (var i = 0; i < messageArray.length; i++) {
            showMessage(messageArray[i]);
        }
    });
}

function connect() {
    var socket = new SockJS('/messaging');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/message', function (message) {
            var parsedMessage = JSON.parse(message.body);
            showMessage(parsedMessage);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var message = $("#message").val();
    $.ajax({
        url: "/messaging",
        type: "POST",
        data: JSON.stringify({
            "content": message,
            "timestamp": moment(new Date()).format("YYYY-MM-DD HH:mm:ssZZ")
        }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        statusCode: {
            400: function() {
                alert("Invalid message!");
            }
        }
    });
}

function showMessage(message) {
    var maxPalindromeLength = message.maxPalindromeLength == undefined ? "" : message.maxPalindromeLength;
    $("#messages").append("<tr><td id=\"" + message.id + "\"></td><td>" + message.timestamp + "</td><td>" + maxPalindromeLength + "</td></tr>");
    $("#" + message.id).text(message.content);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function() {
        connect();
    });
    $("#disconnect").click(function() { disconnect(); });
    $("#send").click(function() { sendMessage(); });
});