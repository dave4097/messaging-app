package org.david.messaging.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.david.messaging.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebBroadcaster {

   public static final String MESSAGE_TOPIC = "/topic/message";
   private SimpMessagingTemplate simpMessagingTemplate;

   @Autowired
   public WebBroadcaster(SimpMessagingTemplate simpMessagingTemplate) {
      this.simpMessagingTemplate = simpMessagingTemplate;
   }

   public void broadcast(Message message) {
      simpMessagingTemplate.convertAndSend(MESSAGE_TOPIC, message);
      log.info("Sent message {} on topic {}", message, MESSAGE_TOPIC);
   }
}
