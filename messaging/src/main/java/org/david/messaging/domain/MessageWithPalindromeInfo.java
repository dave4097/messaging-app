package org.david.messaging.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
@EqualsAndHashCode
public class MessageWithPalindromeInfo {

   public MessageWithPalindromeInfo(Message message, int maxPalindromeLength) {
      this.maxPalindromeLength = maxPalindromeLength;
      this.content = message.getContent();
      this.timestamp = message.getTimestamp();
   }

   private String content;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
   private ZonedDateTime timestamp;

   private int maxPalindromeLength;
}
