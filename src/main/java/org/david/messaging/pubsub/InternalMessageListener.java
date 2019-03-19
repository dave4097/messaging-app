package org.david.messaging.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.david.messaging.domain.ViewableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InternalMessageListener {

   private final WebBroadcaster webBroadcaster;

   @Autowired
   public InternalMessageListener(WebBroadcaster webBroadcaster) {
      this.webBroadcaster = webBroadcaster;
   }

   public void handleMessage(ViewableMessage message) {
      log.info("Received message on internal topic: {}", message);
      webBroadcaster.broadcast(message);
   }
}
