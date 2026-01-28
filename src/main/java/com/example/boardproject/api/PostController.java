package com.example.boardproject.api;

import com.example.boardproject.api.in.PostApi;
import com.example.boardproject.domain.post.dto.request.PostCreateRequest;
import com.example.boardproject.domain.post.dto.request.PostUpdateRequest;
import com.example.boardproject.domain.post.dto.response.PostListResponse;
import com.example.boardproject.domain.post.dto.response.PostResponse;
import com.example.boardproject.domain.post.service.PostService;
import com.example.boardproject.global.common.response.SuccessResponse;
import com.example.boardproject.global.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController implements PostApi {

    private final PostService postService;

    /**
     * 게시글 목록 조회
     */
    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<PostListResponse>>> getPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort) {

        PageResponse<PostListResponse> response = postService.getPostList(page, size, sort);
        return ResponseEntity.ok(SuccessResponse.of(response));
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<PostResponse>> getPost(@PathVariable UUID id) {
        PostResponse response = postService.getPost(id);
        return ResponseEntity.ok(SuccessResponse.of(response));
    }

    /**
     * 게시글 작성
     */
    @PostMapping
    public ResponseEntity<SuccessResponse<PostResponse>> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal UUID userId) {

        PostResponse response = postService.createPost(request, userId);
        return ResponseEntity.ok(SuccessResponse.of(response, "게시글이 작성되었습니다"));
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<PostResponse>> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal UUID userId) {

        PostResponse response = postService.updatePost(id, request, userId);
        return ResponseEntity.ok(SuccessResponse.of(response, "게시글이 수정되었습니다"));
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deletePost(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {

        postService.deletePost(id, userId);
        return ResponseEntity.ok(SuccessResponse.of("게시글이 삭제되었습니다"));
    }
}