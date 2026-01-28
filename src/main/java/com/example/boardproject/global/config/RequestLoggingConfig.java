package com.example.boardproject.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);  // 쿼리 스트링 포함
        filter.setIncludePayload(true);     // 요청 본문 포함
        filter.setMaxPayloadLength(10000);  // 요청 본문 최대 길이 설정
        filter.setIncludeHeaders(true);     // 헤더 포함
        filter.setAfterMessagePrefix("REQUEST DATA : ");  // 로그 메시지 프리픽스
        return filter;
    }
}