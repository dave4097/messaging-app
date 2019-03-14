package org.david.messaging.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeJsonDeserializer extends StdDeserializer<ZonedDateTime> {

   private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");

   public ZonedDateTimeJsonDeserializer() {
      this(null);
   }

   public ZonedDateTimeJsonDeserializer(Class<?> vc) {
      super(vc);
   }

   @Override
   public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
      return ZonedDateTime.parse(jsonParser.getText(), formatter);
   }
}
