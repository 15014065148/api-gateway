package com.eveb.gateway.game.ea.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class Helper {
    private Map<String, String> tokenMap = new ConcurrentHashMap<>();
    private Map<String, String> usernameMap = new ConcurrentHashMap<>();
    private ReentrantLock lock = new ReentrantLock();

    public static String uuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    public void putToken(String key, String value) {
        lock.lock();
        try {
            tokenMap.put(key, value);
            usernameMap.put(value,key);
        }finally {
            lock.unlock();
        }
    }

    public String getToken(String key) {
        return tokenMap.get(key);
    }

    public String getUsername(String key) {
        return usernameMap.get(key);
    }


}

