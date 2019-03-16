package org.david.messaging.domain;

import lombok.extern.slf4j.Slf4j;
import org.david.messaging.pubsub.InternalMessageBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Optional.ofNullable;

@Service
@Slf4j
public class MessagingService {

   private final InternalMessageBroadcaster internalMessageBroadcaster;
   private final ViewableMessageRepository viewableMessageRepository;

   @Autowired
   public MessagingService(InternalMessageBroadcaster internalMessageBroadcaster, ViewableMessageRepository viewableMessageRepository) {
      this.internalMessageBroadcaster = internalMessageBroadcaster;
      this.viewableMessageRepository = viewableMessageRepository;
   }

   public ViewableMessageIterableWrapper getAllMessages() {
      return new ViewableMessageIterableWrapper(viewableMessageRepository.findAll());
   }

   public void processMessage(Message message) {
      log.info("Processing posting {}", message);
      // Moving enrichment of palindrome information to before internal broadcast to improve frontend and ensure
      // code is only run once for a message.
      ViewableMessage viewableMessage = buildViewableMessage(message);
      // Keeping the persisting of messages here rather than moving it to a consumer running on all instances.
      viewableMessageRepository.save(viewableMessage);
      internalMessageBroadcaster.broadcastToAllInstances(viewableMessage);
   }

   private ViewableMessage buildViewableMessage(Message message) {
      String id = UUID.randomUUID().toString();
      return ofNullable(message.getContent())
            .map(content -> content.replaceAll("[^a-zA-Z]", "").toLowerCase())
            .map(content -> new ViewableMessage(id, message, getMaxLengthOfPalindrome(content, 0)))
            .orElse(new ViewableMessage(id, message, 0));
   }

   private int getMaxLengthOfPalindrome(String string, int start) {
      if (start >= string.length()) {
         return 0;
      }

      int palindromeLengthFromStart = getMaxLengthOfPalindromeFromIndex(string, start, start, string.length() - 1, 0);
      int palindromeLengthInRemainder = getMaxLengthOfPalindrome(string, start + 1);

      return palindromeLengthFromStart > palindromeLengthInRemainder ? palindromeLengthFromStart : palindromeLengthInRemainder;
   }

   private int getMaxLengthOfPalindromeFromIndex(String string, int origStart, int start, int end, int length) {
      if (start == end) {
         return length > 0 ? length + 1 : length;
      }

      if (start > end) {
         return length;
      }

      if (string.charAt(start) != string.charAt(end)) {
         return getMaxLengthOfPalindromeFromIndex(string, origStart, origStart, end - 1, 0);
      }

      return getMaxLengthOfPalindromeFromIndex(string, origStart, start + 1, end - 1, length + 2);
   }
}
