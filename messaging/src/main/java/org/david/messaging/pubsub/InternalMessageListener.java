package org.david.messaging.pubsub;

import org.david.messaging.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InternalMessageListener {

   private final WebBroadcaster webBroadcaster;

   @Autowired
   public InternalMessageListener(WebBroadcaster webBroadcaster) {
      this.webBroadcaster = webBroadcaster;
   }

   public void handleMessage(Message message) {
      webBroadcaster.broadcast(message);
   }
}
