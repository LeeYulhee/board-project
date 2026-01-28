package com.example.boardproject.domain.post.entity;

import com.example.boardproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)  // Auditing 필수!
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @EqualsAndHashCode.Include
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "user_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Post(String title, String content, UUID userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    /**
     * 게시글 수정
     */
    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    /**
     * 작성자 확인
     */
    public boolean isAuthor(UUID userId) {
        return this.userId.equals(userId);
    }
}