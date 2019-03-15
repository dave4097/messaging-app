package org.david.messaging.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class MessagingService {

   private final WebBroadcaster webBroadcaster;
   private final MessageRepository messageRepository;

   @Autowired
   public MessagingService(WebBroadcaster webBroadcaster, MessageRepository messageRepository) {
      this.webBroadcaster = webBroadcaster;
      this.messageRepository = messageRepository;
   }

   public Messages getAllMessages() {
      return new Messages(messageRepository.findAll().stream()
            .map(this::enrichWithPalindromeMaxLength)
            .collect(toList()));
   }

   public void processMessage(Message message) {
      log.info("Processing message {}", message);
      messageRepository.save(message);
      webBroadcaster.broadcast(message);
   }

   private MessageWithPalindromeInfo enrichWithPalindromeMaxLength(Message message) {
      return ofNullable(message.getContent())
            .map(content -> content.replaceAll("[^a-zA-Z]", "").toLowerCase())
            .map(content -> new MessageWithPalindromeInfo(message, getMaxLengthOfPalindrome(content, 0)))
            .orElse(new MessageWithPalindromeInfo(message, 0));
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
