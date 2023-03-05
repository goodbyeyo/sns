package com.mysql.study.domain.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalApiService {

    public String getUserName(String userId) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("get userName from other service...");

        if (userId.equals("A")) {
            return "nameA";
        }
        if (userId.equals("B")) {
            return "nameB";
        }
        return "";
    }

    @Cacheable(cacheNames = "userAgeCache", key = "#userId")
    public int getUserAge(String userId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("get userAge from other service...");

        if (userId.equals("A")) {
            return 30;
        }
        if (userId.equals("B")) {
            return 20;
        }
        return 0;
    }
}
