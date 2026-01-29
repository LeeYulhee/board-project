package com.example.boardproject.domain.auth.service;

import com.example.boardproject.domain.auth.dto.request.LoginRequest;
import com.example.boardproject.domain.auth.dto.request.SignupRequest;
import com.example.boardproject.domain.auth.dto.response.SignupResponse;
import com.example.boardproject.domain.auth.dto.response.TokenResponse;
import com.example.boardproject.domain.user.entity.User;
import com.example.boardproject.domain.user.repository.UserRepository;
import com.example.boardproject.global.exception.CustomException;
import com.example.boardproject.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccessTest() {
        // given
        SignupRequest request = SignupRequest.builder()
                .loginId("testUser")
                .password("Password123!")
                .nickname("Tester")
                .build();

        // when
        SignupResponse response = authService.signup(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getLoginId()).isEqualTo(request.getLoginId());
        assertThat(response.getNickname()).isEqualTo(request.getNickname());

        User savedUser = userRepository.findById(response.getUserId()).orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(passwordEncoder.matches(request.getPassword(), savedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("중복 회원가입 실패")
    void signupDuplicatedIdFailTest() {
        // given
        SignupRequest request = SignupRequest.builder()
                .loginId("testUser")
                .password("Password123!")
                .nickname("Tester")
                .build();
        authService.signup(request);

        // when & then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_LOGIN_ID);
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccessTest() {
        // given
        String loginId = "testUser";
        String password = "Password123!";
        SignupRequest signupRequest = SignupRequest.builder()
                .loginId(loginId)
                .password(password)
                .nickname("Tester")
                .build();
        authService.signup(signupRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .loginId(loginId)
                .password(password)
                .build();

        // when
        TokenResponse tokenResponse = authService.login(loginRequest);

        // then
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken()).isNotBlank();
        assertThat(tokenResponse.getRefreshToken()).isNotBlank();
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void loginFailWrongPasswordTest() {
        // given
        String loginId = "testUser";
        SignupRequest signupRequest = SignupRequest.builder()
                .loginId(loginId)
                .password("Password123!")
                .nickname("Tester")
                .build();
        authService.signup(signupRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .loginId(loginId)
                .password("WrongPass123")
                .build();

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_CREDENTIALS);
    }
}
