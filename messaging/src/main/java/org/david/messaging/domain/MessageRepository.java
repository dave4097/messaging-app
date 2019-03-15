package org.david.messaging.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Component
public class MessageRepository {

   private final List<Message> messages = new ArrayList<>();

   void save(Message message) {
      messages.add(message);
   }

   List<Message> findAll() {
      return unmodifiableList(messages);
   }
}
