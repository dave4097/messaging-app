package org.david.messaging.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.david.messaging.json.ZonedDateTimeJsonDeserializer;

import java.time.ZonedDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Message {

   private String content;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
   @JsonDeserialize(using = ZonedDateTimeJsonDeserializer.class)
   private ZonedDateTime timestamp;
}
