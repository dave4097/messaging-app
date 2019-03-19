package org.david.messaging.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.david.messaging.domain.ViewableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebBroadcaster {

   private static final String MESSAGE_TOPIC = "/topic/message";
   private SimpMessagingTemplate simpMessagingTemplate;

   @Autowired
   public WebBroadcaster(SimpMessagingTemplate simpMessagingTemplate) {
      this.simpMessagingTemplate = simpMessagingTemplate;
   }

   public void broadcast(ViewableMessage message) {
      simpMessagingTemplate.convertAndSend(MESSAGE_TOPIC, message);
      log.info("Sent message {} on websocket topic {}", message, MESSAGE_TOPIC);
   }
}
