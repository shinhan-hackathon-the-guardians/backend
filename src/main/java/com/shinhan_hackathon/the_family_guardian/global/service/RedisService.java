package com.shinhan_hackathon.the_family_guardian.global.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;


    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, LocalDateTime endLocalDateTime) {
        Duration expireDuration = Duration.between(LocalDateTime.now(), endLocalDateTime);

        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, expireDuration.getSeconds(), TimeUnit.SECONDS);
    }

    @Transactional(readOnly = true)
    public Optional<String> getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();

        Object result = values.get(key);
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result.toString());

    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}