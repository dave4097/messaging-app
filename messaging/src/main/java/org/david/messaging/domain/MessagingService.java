package org.david.messaging.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Collections;

@Component
@Slf4j
public class MessagingService {

   public Messages getAllMessages() {
      return new Messages(Collections.singletonList(new Message("abrakadabra", ZonedDateTime.now())));
   }

   public void processMessage(Message message) {
      log.info("Processing message {}", message);
   }
}
