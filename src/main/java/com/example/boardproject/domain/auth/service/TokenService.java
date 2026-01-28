package com.example.boardproject.domain.auth.service;

import com.example.boardproject.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    @Value("${jwt.refresh-token-validity}") private long refreshTokenExpireTime;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 토큰 발급 (Access + Refresh)
     */
    public TokenPair generateTokenPair(UUID userId) {
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        // Refresh Token을 Redis에 저장
        saveRefreshToken(userId, refreshToken);

        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * Refresh Token 저장
     */
    private void saveRefreshToken(UUID userId, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(
                key,
                refreshToken,
                refreshTokenExpireTime,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * 토큰 쌍 (Access + Refresh)
     */
    public record TokenPair(String accessToken, String refreshToken) {
    }
}