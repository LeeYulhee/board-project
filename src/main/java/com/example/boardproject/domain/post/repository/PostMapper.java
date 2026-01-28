package com.example.boardproject.domain.post.repository;

import com.example.boardproject.domain.post.dto.request.PostSearchParam;
import com.example.boardproject.domain.post.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostMapper {

    /**
     * 게시글 목록 조회 (페이징, 정렬)
     */
    List<Post> findAllWithPaging(PostSearchParam param);

    /**
     * 게시글 총 개수
     */
    long countAll(PostSearchParam param);

    /**
     * 게시글 상세 조회 (작성자 정보 포함)
     */
    Optional<Post> findByIdWithAuthor(Long postId);
}