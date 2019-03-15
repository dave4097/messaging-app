package org.david.messaging.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;

@Service
@Slf4j
public class MessagingService {

   private final WebBroadcaster webBroadcaster;

   @Autowired
   public MessagingService(WebBroadcaster webBroadcaster) {
      this.webBroadcaster = webBroadcaster;
   }

   public Messages getAllMessages() {
      return new Messages(Collections.singletonList(new Message("abrakadabra", ZonedDateTime.now())));
   }

   public void processMessage(Message message) {
      log.info("Processing message {}", message);
      webBroadcaster.broadcast(message);
   }
}
