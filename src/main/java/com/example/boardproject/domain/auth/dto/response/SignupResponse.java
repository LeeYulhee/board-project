package com.example.boardproject.domain.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupResponse {

    private UUID userId;
    private String loginId;
    private String nickname;

    public static SignupResponse of(UUID userId, String loginId, String nickname) {
        return new SignupResponse(userId, loginId, nickname);
    }
}