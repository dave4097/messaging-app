package org.david.messaging.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Optional.ofNullable;

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
      String text = jsonParser.getText();
      return ofNullable(text)
            .map(t -> ZonedDateTime.parse(t, formatter))
            .orElse(null);
   }
}
