package com.example.boardproject.api.in;

import com.example.boardproject.domain.post.dto.request.PostCreateRequest;
import com.example.boardproject.domain.post.dto.request.PostUpdateRequest;
import com.example.boardproject.domain.post.dto.response.PostListResponse;
import com.example.boardproject.domain.post.dto.response.PostResponse;
import com.example.boardproject.global.common.response.ErrorResponse;
import com.example.boardproject.global.common.response.PageResponse;
import com.example.boardproject.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "posts", description = "게시글 API")
public interface PostApi {

    @Operation(summary = "게시글 목록 조회", description = "페이징과 정렬 옵션을 적용하여 게시글 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PageResponse.class)))
    })
    ResponseEntity<SuccessResponse<PageResponse<PostListResponse>>> getPostList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 방식 (latest: 최신순, oldest: 오래된순)", example = "latest")
            @RequestParam(defaultValue = "latest") String sort);

    @Operation(summary = "게시글 상세 조회", description = "게시글 ID로 특정 게시글의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse<PostResponse>> getPost(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable UUID id);

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검증 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse<PostResponse>> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal UUID userId);

    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다. 작성자만 수정할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검증 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음 (작성자가 아님)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse<PostResponse>> updatePost(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal UUID userId);

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다. 작성자만 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음 (작성자가 아님)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<SuccessResponse<Void>> deletePost(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId);
}
