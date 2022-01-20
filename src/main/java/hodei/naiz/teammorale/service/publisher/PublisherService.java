package hodei.naiz.teammorale.service.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;

/**
 * Created by Hodei Eceiza
 * Date: 1/20/2022
 * Time: 10:06
 * Project: TeamMorale
 * Copyright: MIT
 */
@Service
public class PublisherService {

    private ReactiveRedisOperations<String, EmailServiceMessage> redisTemplate;

    @Value("${topic.name:email-notification}")
    private String topic;

    public PublisherService(ReactiveRedisOperations<String, EmailServiceMessage> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void sendEmail(EmailServiceMessage emailServiceMessage){
        redisTemplate.convertAndSend(topic, emailServiceMessage).subscribe();

    }

}
