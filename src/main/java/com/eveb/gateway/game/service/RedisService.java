package com.eveb.gateway.game.service;

import com.eveb.gateway.config.CacheDuration;
import com.eveb.gateway.constants.ApplicationConstants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Author: Miracle
 * @Description:
 * @Date: 2018/9/3 10:11
 **/
@Service
public class RedisService {
    @Resource(name = "redisTemplate")
    RedisTemplate<String, Object> redisTemplate;

    public void setRedisValue(String key, Object var) {
        redisTemplate.opsForValue().set(ApplicationConstants.PROJECT_NAME+":"+key, var);
    }

    public Object getRedisValus(String key) {
        return redisTemplate.opsForValue().get(ApplicationConstants.PROJECT_NAME+":"+key);
    }

    public Boolean booleanRedis(String key) {
        if (Objects.isNull(redisTemplate.opsForValue().get(ApplicationConstants.PROJECT_NAME+":"+key))) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public long del(final String... keys) {
        return (Long) redisTemplate.execute((RedisCallback<?>) connection -> {
            long result = 0;
            for (int i = 0; i < keys.length; i++) {
                result = connection.del((ApplicationConstants.PROJECT_NAME+":"+keys[i]).getBytes());
            }
            return result;
        });
    }
}
