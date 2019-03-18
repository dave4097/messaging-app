package org.david.messaging.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.david.messaging.domain.ViewableMessage;
import org.david.messaging.pubsub.InternalMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableRedisRepositories(basePackages = "org.david.messaging.domain")
public class RedisConfig {

   @Value("${spring.redis.host}")
   private String REDIS_HOSTNAME;

   @Value("${spring.redis.port}")
   private int REDIS_PORT;

   @Bean
   public JedisConnectionFactory jedisConnectionFactory() {
      RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(REDIS_HOSTNAME, REDIS_PORT);
      JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling().build();
      return new JedisConnectionFactory(configuration, jedisClientConfiguration);
   }

   @Bean
   public RedisSerializer<ViewableMessage> redisJsonSerializer(ObjectMapper objectMapper) {
      Jackson2JsonRedisSerializer<ViewableMessage> serializer = new Jackson2JsonRedisSerializer<>(ViewableMessage.class);
      serializer.setObjectMapper(objectMapper);
      return serializer;
   }

   @Bean
   public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory,
                                                      RedisSerializer<ViewableMessage> redisJsonSerializer) {
      final RedisTemplate<String, Object> template = new RedisTemplate<>();
      template.setConnectionFactory(jedisConnectionFactory);
      template.setValueSerializer(redisJsonSerializer);
      return template;
   }

   @Bean
   public MessageListenerAdapter messageListenerAdapter(InternalMessageListener internalMessageListener,
                                                        RedisSerializer<ViewableMessage> redisJsonSerializer) {
      MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(internalMessageListener);
      messageListenerAdapter.setSerializer(redisJsonSerializer);
      return messageListenerAdapter;
   }

   @Bean
   public RedisMessageListenerContainer redisContainer(JedisConnectionFactory jedisConnectionFactory,
                                                       MessageListenerAdapter messageListenerAdapter,
                                                       ChannelTopic topic) {
      final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
      container.setConnectionFactory(jedisConnectionFactory);
      container.addMessageListener(messageListenerAdapter, topic);
      return container;
   }

   @Bean
   public ChannelTopic topic() {
      return new ChannelTopic("pubsub:messages");
   }
}
