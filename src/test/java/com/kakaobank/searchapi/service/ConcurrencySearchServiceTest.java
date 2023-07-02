package com.kakaobank.searchapi.service;

import org.jmock.lib.concurrent.Blitzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class ConcurrencySearchServiceTest {

    int actionCount = 10000;
    int threadCount = 1000;

    //10000개의 thread 로 10 action
    Blitzer blitzer = new Blitzer(actionCount, threadCount);

    @Test
    @DisplayName("동시성 테스트")
    @Disabled
    void storeSearchKeyWord() throws InterruptedException {
        String keyword = "abcd";

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        blitzer.blitz(() -> {
            map.putIfAbsent(keyword, 0);
            map.merge(keyword, 1, Integer::sum);
        });

        Assertions.assertEquals(map.get(keyword), actionCount);
    }
}