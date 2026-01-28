package com.example.boardproject.global.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "페이징 응답")
public class PageResponse<T> {

    @Schema(description = "콘텐츠 목록")
    private List<T> content;

    @Schema(description = "페이지 정보")
    private PageInfo pageInfo;

    @Getter
    @AllArgsConstructor
    @Schema(description = "페이지 메타 정보")
    public static class PageInfo {
        @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
        private int page;
        @Schema(description = "페이지당 항목 수", example = "10")
        private int size;
        @Schema(description = "전체 항목 수", example = "100")
        private long totalElements;
        @Schema(description = "전체 페이지 수", example = "10")
        private int totalPages;
        @Schema(description = "첫 페이지 여부", example = "true")
        private boolean first;
        @Schema(description = "마지막 페이지 여부", example = "false")
        private boolean last;
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