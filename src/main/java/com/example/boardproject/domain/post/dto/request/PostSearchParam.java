package com.example.boardproject.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSearchParam {

    private int page;        // 페이지 번호(0부터 시작)
    private int size;        // 페이지 크기
    private String sort;     // 정렬(latest, oldest)

    // MyBatis에서 사용할 offset 계산
    public int getOffset() {
        return page * size;
    }

    // 정렬 방향 (DESC, ASC)
    public String getSortDirection() {
        return "oldest".equals(sort) ? "ASC" : "DESC";
    }
}