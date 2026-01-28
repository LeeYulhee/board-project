package com.example.boardproject.domain.auth.service;

import com.example.boardproject.domain.auth.dto.request.LoginRequest;
import com.example.boardproject.domain.auth.dto.request.SignupRequest;
import com.example.boardproject.domain.auth.dto.response.SignupResponse;
import com.example.boardproject.domain.auth.dto.response.TokenResponse;
import com.example.boardproject.domain.user.entity.User;
import com.example.boardproject.domain.user.service.UserService;
import com.example.boardproject.global.exception.CustomException;
import com.example.boardproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // 중복 확인
        userService.validateDuplicateLoginId(request.getLoginId());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 생성
        User user = userService.createUser(
                request.getLoginId(),
                encodedPassword,
                request.getNickname()
        );

        return SignupResponse.of(user.getUserId(), user.getLoginId(), user.getNickname());
    }

    /**
     * 로그인
     */
    public TokenResponse login(LoginRequest request) {
        // 사용자 조회
        User user = userService.getUserByLoginId(request.getLoginId());

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 토큰 발급
        TokenService.TokenPair tokenPair = tokenService.generateTokenPair(user.getUserId());

        return TokenResponse.of(tokenPair.accessToken(), tokenPair.refreshToken());
    }
}