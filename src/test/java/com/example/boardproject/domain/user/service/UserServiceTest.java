package com.example.boardproject.domain.user.service;

import com.example.boardproject.domain.user.entity.User;
import com.example.boardproject.domain.user.repository.UserRepository;
import com.example.boardproject.global.exception.CustomException;
import com.example.boardproject.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        // 기본 테스트용 유저 생성
        savedUser = userService.createUser("testUser", "password123!", "tester");
    }

    @Test
    @DisplayName("유저 생성 성공")
    void createUserTest() {
        // given
        String loginId = "new_user";
        String password = "password";
        String nickname = "new_nick";

        // when
        User user = userService.createUser(loginId, password, nickname);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getLoginId()).isEqualTo(loginId);
        assertThat(user.getNickname()).isEqualTo(nickname);
        // DB에도 저장되었는지 확인
        User foundUser = userRepository.findByLoginId(loginId).orElse(null);
        assertThat(foundUser).isNotNull();
    }

    @Test
    @DisplayName("유저 아이디로 유저 조회 성공")
    void getUserByUserIdTest() {
        // when
        User foundUser = userService.getUserById(savedUser.getUserId());

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(foundUser.getLoginId()).isEqualTo(savedUser.getLoginId());
    }

    @Test
    @DisplayName("로그인 아이디로 유저 조회 성공")
    void getUserByLoginIdTest() {
        // when
        User foundUser = userService.getUserByLoginId(savedUser.getLoginId());

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getLoginId()).isEqualTo(savedUser.getLoginId());
    }

    @Test
    @DisplayName("중복된 로그인 아이디 감지")
    void validateDuplicateLoginIdTest() {
        // when & then
        assertThatThrownBy(() -> userService.validateDuplicateLoginId(savedUser.getLoginId()))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_LOGIN_ID);
    }

    @Test
    @DisplayName("중복되지 않은 로그인 아이디 확인")
    void validateDuplicateLoginIdSuccessTest() {
        // when & then (예외가 발생하지 않아야 함)
        userService.validateDuplicateLoginId("unique_id");
    }
}
