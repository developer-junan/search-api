package com.kakaobank.searchapi.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /*@Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }*/
}
