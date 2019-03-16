package org.david.messaging;

import lombok.extern.slf4j.Slf4j;
import org.david.messaging.domain.Message;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(Application.class)
@Slf4j
public class ApplicationIntegrationTest {

   private static RedisServer redisServer;

   @Autowired
   private MockMvc mockMvc;
   @LocalServerPort
   private int port;
   private String url;
   private CompletableFuture<Message> completableFuture;

   @BeforeClass
   public static void startRedisServer() throws IOException {
      redisServer = new RedisServer(6380);
      redisServer.start();
      log.info("Redis server started on {}, active={}", redisServer.ports(), redisServer.isActive());
   }

   @AfterClass
   public static void stopRedisServer() {
      redisServer.stop();
   }

   @Before
   public void setup() {
      completableFuture = new CompletableFuture<>();
      url = "ws://localhost:" + port + "/messaging";
   }

   @Test
   public void shouldOutputPostedMessageToWebSocket() throws Exception {
      WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
      stompClient.setMessageConverter(new MappingJackson2MessageConverter());

      log.info("Connecting to {}", url);
      StompSession stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {
      }).get(1, TimeUnit.SECONDS);

      stompSession.subscribe("/topic/message", new MessagingStompFrameHandler());

      mockMvc.perform(
            post("/messaging")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"content\": \"abrakadabra\",\"timestamp\": \"2018-10-09 00:12:12+0100\"}"));

      Message message = completableFuture.get(5, TimeUnit.SECONDS);

      assertThat(message).isNotNull();
   }

   private List<Transport> createTransportClient() {
      List<Transport> transports = new ArrayList<>(1);
      transports.add(new WebSocketTransport(new StandardWebSocketClient()));
      return transports;
   }

   private class MessagingStompFrameHandler implements StompFrameHandler {
      @Override
      public Type getPayloadType(StompHeaders stompHeaders) {
         return Message.class;
      }

      @Override
      public void handleFrame(StompHeaders stompHeaders, Object o) {
         completableFuture.complete((Message) o);
      }
   }
}
