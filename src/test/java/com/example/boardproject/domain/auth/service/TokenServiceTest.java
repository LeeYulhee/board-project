package com.example.boardproject.domain.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class TokenServiceTest {

    @Container
    static final GenericContainer<?> REDIS =
            new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));
    }

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    @BeforeEach
    void cleanRedis() {
        // 테스트 간 간섭 방지
        stringRedisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("토큰 쌍 발급 및 Redis 저장 성공")
    void generateTokenPairTest() {
        // given
        UUID userId = UUID.randomUUID();

        // when
        TokenService.TokenPair tokenPair = tokenService.generateTokenPair(userId);

        // then
        // 1) 토큰 발급 확인
        assertThat(tokenPair).isNotNull();
        assertThat(tokenPair.accessToken()).isNotBlank();
        assertThat(tokenPair.refreshToken()).isNotBlank();

        // 2) Redis 저장 확인
        String expectedKey = "refresh_token:" + userId;
        String savedValue = stringRedisTemplate.opsForValue().get(expectedKey);

        assertThat(savedValue).isEqualTo(tokenPair.refreshToken());

        // 3) TTL 확인 (약간의 오차 허용)
        Long ttlMs = stringRedisTemplate.getExpire(expectedKey, TimeUnit.MILLISECONDS);
        assertThat(ttlMs).isNotNull();
        assertThat(ttlMs).isGreaterThan(0L);
        // refreshTokenValidity와 거의 같아야 함 (테스트 실행 시간 만큼 감소)
        assertThat(ttlMs).isLessThanOrEqualTo(refreshTokenValidity);
        assertThat(ttlMs).isGreaterThan(refreshTokenValidity - 5_000); // 5초 오차 허용
    }
}
