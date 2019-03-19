package org.david.messaging.pubsub;

import org.david.messaging.domain.ViewableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class InternalMessageBroadcaster {

   private final RedisTemplate<String, Object> redisTemplate;
   private final ChannelTopic channelTopic;

   @Autowired
   public InternalMessageBroadcaster(RedisTemplate<String, Object> redisTemplate, ChannelTopic channelTopic) {
      this.redisTemplate = redisTemplate;
      this.channelTopic = channelTopic;
   }

   public void broadcastToAllInstances(ViewableMessage message) {
      redisTemplate.convertAndSend(channelTopic.getTopic(), message);
   }
}
