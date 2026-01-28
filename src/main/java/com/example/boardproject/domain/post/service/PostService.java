package com.example.boardproject.domain.post.service;

import com.example.boardproject.domain.post.dto.request.PostCreateRequest;
import com.example.boardproject.domain.post.dto.request.PostSearchParam;
import com.example.boardproject.domain.post.dto.request.PostUpdateRequest;
import com.example.boardproject.domain.post.dto.response.PostListResponse;
import com.example.boardproject.domain.post.dto.response.PostResponse;
import com.example.boardproject.domain.post.entity.Post;
import com.example.boardproject.domain.post.repository.PostMapper;
import com.example.boardproject.domain.post.repository.PostRepository;
import com.example.boardproject.domain.user.entity.User;
import com.example.boardproject.domain.user.service.UserService;
import com.example.boardproject.global.common.PageResponse;
import com.example.boardproject.global.exception.CustomException;
import com.example.boardproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    /**
     * 게시글 목록 조회 (페이징, 정렬)
     */
    public PageResponse<PostListResponse> getPostList(int page, int size, String sort) {
        PostSearchParam param = PostSearchParam.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .build();

        List<Post> posts = postMapper.findAllWithPaging(param);
        long totalElements = postMapper.countAll(param);

        List<PostListResponse> content = posts.stream()
                .map(PostListResponse::from)
                .toList();

        return PageResponse.of(content, page, size, totalElements);
    }

    /**
     * 게시글 상세 조회
     */
    public PostResponse getPost(Long postId) {
        Post post = postMapper.findByIdWithAuthor(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return PostResponse.from(post);
    }

    /**
     * 게시글 작성
     */
    @Transactional
    public PostResponse createPost(PostCreateRequest request, UUID userId) {
        // 작성자 확인
        User user = userService.getUserById(userId);

        // 게시글 생성
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .userId(userId)
                .build();

        Post savedPost = postRepository.save(post);

        return PostResponse.of(savedPost, user);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public PostResponse updatePost(UUID postId, PostUpdateRequest request, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 작성자 확인
        if (!post.isAuthor(userId)) {
            throw new CustomException(ErrorCode.POST_FORBIDDEN);
        }

        // 수정
        post.updatePost(request.getTitle(), request.getContent());

        // 작성자 조회
        User user = userService.getUserById(userId);

        return PostResponse.of(post, user);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 작성자 확인
        if (!post.isAuthor(userId)) {
            throw new CustomException(ErrorCode.POST_FORBIDDEN);
        }

        postRepository.delete(post);
    }
}