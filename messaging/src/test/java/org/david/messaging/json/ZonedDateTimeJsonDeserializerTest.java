package org.david.messaging.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.base.ParserBase;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZonedDateTimeJsonDeserializerTest {

   private ZonedDateTimeJsonDeserializer deserializer = new ZonedDateTimeJsonDeserializer();

   @Test
   public void shouldConvertStringToZonedDateTimeWithoutError() throws Exception {
      JsonParser jsonParser = mock(ParserBase.class);

      when(jsonParser.getText()).thenReturn("2018-10-09 00:12:12+0100");

      ZonedDateTime zonedDateTime = deserializer.deserialize(jsonParser, null);

      assertThat(zonedDateTime.getOffset()).isNotNull();
   }
}