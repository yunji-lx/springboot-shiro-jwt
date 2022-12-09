package com.howie.shirojwt.config;

import com.howie.shirojwt.util.MyRedisSerializer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        MyRedisSerializer myRedisSerializer = new MyRedisSerializer(Object.class);

        template.setKeySerializer(stringRedisSerializer);
        // 自定义序列化
        template.setValueSerializer(myRedisSerializer);

        template.setHashKeySerializer(stringRedisSerializer);
        // 自定义序列化
        template.setHashValueSerializer(myRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

}