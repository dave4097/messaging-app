package org.david.messaging.domain;

import org.david.messaging.pubsub.InternalMessageBroadcaster;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessagingServiceTest {

   @Mock
   private InternalMessageBroadcaster internalMessageBroadcaster;

   @Mock
   private MessageRepository messageRepository;

   private MessagingService messagingService;

   @Before
   public void setUp() {
      messagingService = new MessagingService(internalMessageBroadcaster, messageRepository);
   }

   @Test
   public void shouldEnrichMessageWithPalindromeLengthWhenGettingAllMessages() {
      Message message1 = new Message("", null);
      Message message2 = new Message("ada", null);
      Message message3 = new Message("xyz", null);
      Message message4 = new Message("abrakadabra", null);
      Message message5 = new Message("a", null);
      Message message6 = new Message("A man, a plan, a canal, Panama!", null);
      Message message7 = new Message(null, null);

      when(messageRepository.findAll()).thenReturn(asList(
            message1, message2, message3, message4, message5, message6, message7));

      Messages allMessages = messagingService.getAllMessages();

      assertThat(allMessages.getMessages()).containsExactlyInAnyOrder(
            new MessageWithPalindromeInfo(message1, 0),
            new MessageWithPalindromeInfo(message2, 3),
            new MessageWithPalindromeInfo(message3, 0),
            new MessageWithPalindromeInfo(message4, 3),
            new MessageWithPalindromeInfo(message5, 0),
            new MessageWithPalindromeInfo(message6, 21),
            new MessageWithPalindromeInfo(message7, 0)
      );
   }

   @Test
   public void shouldProcessMessageByPersistingAndBroadcastToWebClients() {
      ZonedDateTime now = ZonedDateTime.now();
      Message message = new Message("abc", now);

      messagingService.processMessage(message);

      InOrder inOrder = inOrder(messageRepository, internalMessageBroadcaster);
      inOrder.verify(messageRepository).save(message);
      inOrder.verify(internalMessageBroadcaster).broadcastToAllInstances(message);
   }
}