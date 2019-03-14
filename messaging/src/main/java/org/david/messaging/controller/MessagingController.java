package org.david.messaging.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.david.messaging.domain.Message;
import org.david.messaging.domain.Messages;
import org.david.messaging.domain.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(
      value = "Messaging",
      description = "Operations to get and post messages"
)
@RestController
@RequestMapping("/messaging")
public class MessagingController {

   private final MessagingService messagingService;

   @Autowired
   public MessagingController(MessagingService messagingService) {
      this.messagingService = messagingService;
   }

   @ApiOperation(
         value = "Gets all messages that have been posted",
         httpMethod = "GET",
         produces = "application/JSON"
   )
   @GetMapping
   public Messages getAllMessages() {
      return messagingService.getAllMessages();
   }

   @ApiOperation(
         value = "Posts a message",
         httpMethod = "POST",
         consumes = "application/JSON"
   )
   @PostMapping
   public void postMessage(@RequestBody Message message) {
      messagingService.processMessage(message);
   }
}
