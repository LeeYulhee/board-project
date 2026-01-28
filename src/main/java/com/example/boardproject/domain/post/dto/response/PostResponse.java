package com.example.boardproject.domain.post.dto.response;

import com.example.boardproject.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponse {

    private UUID postId;
    private String title;
    private String content;
    private AuthorInfo author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                AuthorInfo.from(post.getUser()),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}