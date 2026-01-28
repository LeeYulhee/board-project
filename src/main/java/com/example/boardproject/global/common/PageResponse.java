package com.example.boardproject.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private PageInfo pageInfo;

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private int page;           // 현재 페이지 (0부터 시작)
        private int size;           // 페이지 크기
        private long totalElements; // 전체 요소 수
        private int totalPages;     // 전체 페이지 수
        private boolean first;      // 첫 페이지 여부
        private boolean last;       // 마지막 페이지 여부
    }

    /**
     * Spring Data의 Page 객체로부터 생성
     */
    public static <T> PageResponse<T> of(org.springframework.data.domain.Page<T> page) {
        PageInfo pageInfo = new PageInfo(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );

        return new PageResponse<>(page.getContent(), pageInfo);
    }

    /**
     * 직접 생성
     */
    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean first = page == 0;
        boolean last = page >= totalPages - 1;

        PageInfo pageInfo = new PageInfo(page, size, totalElements, totalPages, first, last);

        return new PageResponse<>(content, pageInfo);
    }
}