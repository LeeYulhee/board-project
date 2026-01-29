package com.example.boardproject.domain.post.service;

import com.example.boardproject.domain.post.dto.request.PostCreateRequest;
import com.example.boardproject.domain.post.dto.request.PostUpdateRequest;
import com.example.boardproject.domain.post.dto.response.PostListResponse;
import com.example.boardproject.domain.post.dto.response.PostResponse;
import com.example.boardproject.domain.user.entity.User;
import com.example.boardproject.domain.user.service.UserService;
import com.example.boardproject.global.common.response.PageResponse;
import com.example.boardproject.global.exception.CustomException;
import com.example.boardproject.global.exception.ErrorCode;
import jakarta.persistence.EntityManager;
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
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userService.createUser("postWriter", "password", "writer");
    }

    @Test
    @DisplayName("게시글 작성 성공")
    void createPostTest() {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("Test Title")
                .content("Test Content")
                .build();

        // when
        PostResponse response = postService.createPost(request, savedUser.getUserId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Title");
        assertThat(response.getAuthor().getNickname()).isEqualTo("writer");
    }

    @Test
    @DisplayName("게시글 단건 조회 성공")
    void getPostTest() {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("Test Title")
                .content("Test Content")
                .build();
        PostResponse created = postService.createPost(request, savedUser.getUserId());

        // JPA 영속성 컨텍스트 내용을 DB에 반영하여 MyBatis가 읽을 수 있도록 함 (Service 내부 로직에 flush가 없다면)
        entityManager.flush();
        entityManager.clear();

        // when
        PostResponse found = postService.getPost(created.getPostId());

        // then
        assertThat(found).isNotNull();
        assertThat(found.getPostId()).isEqualTo(created.getPostId());
        assertThat(found.getTitle()).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("게시글 목록 조회 성공")
    void getPostListTest() {
        // given
        for (int i = 0; i < 5; i++) {
            postService.createPost(PostCreateRequest.builder()
                    .title("Title " + i)
                    .content("Content " + i)
                    .build(), savedUser.getUserId());
        }
        entityManager.flush();
        entityManager.clear();

        // when
        PageResponse<PostListResponse> pageResponse = postService.getPostList(0, 10, "latest");

        // then
        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.getPageInfo().getTotalElements()).isEqualTo(5);
        assertThat(pageResponse.getContent()).hasSize(5);
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePostTest() {
        // given
        PostResponse created = postService.createPost(PostCreateRequest.builder()
                .title("Original")
                .content("Original Content")
                .build(), savedUser.getUserId());

        entityManager.flush();
        entityManager.clear();

        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("Updated")
                .content("Updated Content")
                .build();

        // when
        PostResponse updated = postService.updatePost(created.getPostId(), updateRequest, savedUser.getUserId());

        // then
        assertThat(updated.getTitle()).isEqualTo("Updated");
        assertThat(updated.getContent()).isEqualTo("Updated Content");
    }

    @Test
    @DisplayName("권한 없는 유저의 게시글 수정 실패")
    void updatePostForbiddenTest() {
        // given
        PostResponse created = postService.createPost(PostCreateRequest.builder()
                .title("Original")
                .content("Content")
                .build(), savedUser.getUserId());

        User otherUser = userService.createUser("other", "pass", "other");

        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("Updated")
                .content("Updated Content")
                .build();

        // when & then
        assertThatThrownBy(() -> postService.updatePost(created.getPostId(), updateRequest, otherUser.getUserId()))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.POST_FORBIDDEN);
    }
}
