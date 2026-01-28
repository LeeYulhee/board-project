package com.example.boardproject.domain.post.dto.response;

import com.example.boardproject.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorInfo {

    private UUID userId;
    private String nickname;

    public static AuthorInfo from(User user) {
        return new AuthorInfo(user.getUserId(), user.getNickname());
    }
}