package org.david.messaging.controller;

import org.david.messaging.domain.Message;
import org.david.messaging.domain.MessageWithPalindromeInfo;
import org.david.messaging.domain.Messages;
import org.david.messaging.domain.MessagingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import(MessagingController.class)
public class MessagingControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @MockBean
   private MessagingService messagingService;

   @Test
   public void shouldPostMessage() throws Exception {
      mockMvc.perform(
            post("/messaging")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"content\": \"abrakadabra\",\"timestamp\": \"2018-10-09 00:12:12+0100\"}"))
            .andDo(print())
            .andExpect(status().isOk());

      verify(messagingService).processMessage(new Message(
            "abrakadabra", ZonedDateTime.parse("2018-10-09T00:12:12+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
   }

   @Test
   public void shouldGetAllMessages() throws Exception {
      ZonedDateTime zonedDateTime = ZonedDateTime.parse("2018-10-09T00:12:12+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
      MessageWithPalindromeInfo message = new MessageWithPalindromeInfo(
            new Message("abrakadabra", zonedDateTime), 3);
      when(messagingService.getAllMessages()).thenReturn(new Messages(singletonList(message)));

      mockMvc.perform(get("/messaging"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content()
                  .json("{\"messages\": [{\"content\": \"abrakadabra\",\"timestamp\": \"2018-10-09 00:12:12+0100\"}]}"));
   }
}