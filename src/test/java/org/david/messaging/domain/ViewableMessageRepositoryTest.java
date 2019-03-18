package org.david.messaging.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.david.messaging.configuration.RedisConfig;
import org.david.messaging.pubsub.InternalMessageListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RedisConfig.class, ViewableMessageRepositoryTest.TestConfig.class})
@TestPropertySource("classpath:application.properties")
public class ViewableMessageRepositoryTest {

   private static RedisServer redisServer;

   @Autowired
   private ViewableMessageRepository viewableMessageRepository;

   @BeforeClass
   public static void startRedisServer() throws IOException {
      redisServer = new redis.embedded.RedisServer(6380);
      redisServer.start();
   }

   @AfterClass
   public static void stopRedisServer() {
      redisServer.stop();
   }

   @Test
   public void shouldPersistAndGetMessage() {
      ViewableMessage posting = new ViewableMessage("1", "abrakadabra", ZonedDateTime.now(), 3);

      viewableMessageRepository.save(posting);

      assertThat(viewableMessageRepository.findAll()).contains(posting);
   }

   public static class TestConfig {

      @Bean
      public ObjectMapper objectMapper() {
         return new ObjectMapper();
      }

      @Bean
      public InternalMessageListener internalMessageListener() {
         return new InternalMessageListener(null);
      }
   }
}