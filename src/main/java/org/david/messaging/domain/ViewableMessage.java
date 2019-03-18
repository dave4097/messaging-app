package org.david.messaging.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.david.messaging.json.ZonedDateTimeJsonDeserializer;
import org.springframework.data.redis.core.RedisHash;

import java.time.ZonedDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash("ViewableMessage")
public class ViewableMessage {

   private String id;

   private String content;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
   @JsonDeserialize(using = ZonedDateTimeJsonDeserializer.class)
   private ZonedDateTime timestamp;

   private int maxPalindromeLength;

   public ViewableMessage(String id, Message message, int maxPalindromeLength) {
      this.id = id;
      this.content = message.getContent();
      this.timestamp = message.getTimestamp();
      this.maxPalindromeLength = maxPalindromeLength;
   }
}
