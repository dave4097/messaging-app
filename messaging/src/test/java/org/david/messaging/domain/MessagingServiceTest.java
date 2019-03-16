package org.david.messaging.domain;

import org.david.messaging.pubsub.InternalMessageBroadcaster;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessagingServiceTest {

   @Mock
   private InternalMessageBroadcaster internalMessageBroadcaster;

   @Mock
   private ViewableMessageRepository viewableMessageRepository;

   private MessagingService messagingService;

   @Before
   public void setUp() {
      messagingService = new MessagingService(internalMessageBroadcaster, viewableMessageRepository);
   }

   @Test
   public void shouldPersistAndInternallyBroadcastMessage() {
      messagingService.processMessage(new Message("ada", null));

      InOrder inOrder = inOrder(viewableMessageRepository, internalMessageBroadcaster);
      inOrder.verify(viewableMessageRepository).save(any(ViewableMessage.class));
      inOrder.verify(internalMessageBroadcaster).broadcastToAllInstances(any(ViewableMessage.class));
   }

   @Test
   public void shouldEnrichWithMaxPalindromeLengthWhenContentIsEmptyString() {
      testMaxPalindromeLengthEnrichment("", 0);
   }

   @Test
   public void shouldEnrichWithMaxPalindromeLengthWhenContentIsACompletePalindrome() {
      testMaxPalindromeLengthEnrichment("ada", 3);
   }

   @Test
   public void shouldEnrichWithMaxPalindromeLengthWhenContentHasNoPalindrome() {
      testMaxPalindromeLengthEnrichment("xyz", 0);
   }

   @Test
   public void shouldEnrichWithMaxPalindromeLengthWhenContentContainsSinglePalindrome() {
      testMaxPalindromeLengthEnrichment("abrakadabra", 3);
   }

   @Test
   public void shouldEnrichWithMaxPalindromeLengthWhenContentIsSingleCharacter() {
      // Need to confirm if this is expected!
      testMaxPalindromeLengthEnrichment("a", 0);
   }

   @Test
   public void shouldEnrichWithMaxPalindromeLengthWhenContentContainsNonAlphabeticCharacters() {
      testMaxPalindromeLengthEnrichment("A man, a plan, a canal, Panama!", 21);
   }

   @Test
   public void shouldEnrichWithMaxPalindromeLengthWhenContentIsNull() {
      testMaxPalindromeLengthEnrichment(null, 0);
   }

   @Test
   public void shouldEnrichWithMaxPalindromeLengthWhenContentContainsMoreThanOnePalindrome() {
      testMaxPalindromeLengthEnrichment("abbarakadabra", 4);
   }

   private void testMaxPalindromeLengthEnrichment(String ada, int i) {
      Message message = new Message(ada, null);

      messagingService.processMessage(message);

      ArgumentCaptor<ViewableMessage> argument = ArgumentCaptor.forClass(ViewableMessage.class);
      verify(viewableMessageRepository).save(argument.capture());
      assertThat(argument.getValue().getMaxPalindromeLength()).isEqualTo(i);
   }
}