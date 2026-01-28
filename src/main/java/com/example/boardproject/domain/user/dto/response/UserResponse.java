package com.example.boardproject.domain.user.dto.response;

import com.example.boardproject.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    private UUID userId;
    private String loginId;
    private String nickname;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getLoginId(),
                user.getNickname(),
                user.getCreatedAt()
        );
    }
}