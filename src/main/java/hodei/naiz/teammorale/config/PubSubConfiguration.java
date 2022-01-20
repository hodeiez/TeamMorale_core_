package hodei.naiz.teammorale.config;

import hodei.naiz.teammorale.service.publisher.EmailServiceMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created by Hodei Eceiza
 * Date: 1/20/2022
 * Time: 10:05
 * Project: TeamMorale
 * Copyright: MIT
 */
@Configuration
public class PubSubConfiguration {

    @Bean
    public ReactiveRedisOperations<String, EmailServiceMessage> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisSerializer<EmailServiceMessage> valueSerializer = new Jackson2JsonRedisSerializer<>(EmailServiceMessage.class);
        RedisSerializationContext<String, EmailServiceMessage> serializationContext = RedisSerializationContext.<String,EmailServiceMessage>newSerializationContext(RedisSerializer.string())
                .value(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(lettuceConnectionFactory, serializationContext);
    }
}
