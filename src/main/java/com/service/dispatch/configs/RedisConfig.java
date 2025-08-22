package com.service.dispatch.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * LettuceConnectionFactory: Quản lý kết nối tới redis. StringRedisTemplate: Dùng để dễ dàng thao
     * tác với dữ liệu kiểu chuỗi với Redis. Các serializer giúp định dạng các dữ liệu lưu trữ:
     * StringRedisSerializer: Chỉ dùng khóa (key) theo dạng chuỗi GenericToStringSerializer: Chuyển
     * đổi hash thành key dạng chuỗi Jackson2JsonRedisSerializer: Chuyển đổi giá trị thành JSON để lưu
     * trữ dễ mở rộng và linh hoạt.
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory factory) {
        return this.cofigureStringRedisTemplate(factory);
    }

    private StringRedisTemplate cofigureStringRedisTemplate(LettuceConnectionFactory factory) {
        final StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(String.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
}
