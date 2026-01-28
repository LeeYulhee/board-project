package com.example.boardproject.domain.user.service;

import com.example.boardproject.domain.user.entity.User;
import com.example.boardproject.domain.user.repository.UserRepository;
import com.example.boardproject.global.exception.CustomException;
import com.example.boardproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 ID로 조회
     */
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 로그인 ID로 조회
     */
    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 사용자 생성
     */
    @Transactional
    public User createUser(String loginId, String password, String nickname) {
        User user = User.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .build();

        return userRepository.save(user);
    }

    /**
     * 로그인 ID 중복 확인
     */
    public void validateDuplicateLoginId(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }
    }
}