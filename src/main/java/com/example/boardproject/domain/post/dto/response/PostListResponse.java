package com.example.boardproject.domain.post.dto.response;

import com.example.boardproject.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostListResponse {

    private UUID postId;
    private String title;
    private AuthorInfo author;
    private LocalDateTime createdAt;

    public static PostListResponse from(Post post) {
        return new PostListResponse(
                post.getPostId(),
                post.getTitle(),
                AuthorInfo.from(post.getUser()),
                post.getCreatedAt()
        );
    }
}