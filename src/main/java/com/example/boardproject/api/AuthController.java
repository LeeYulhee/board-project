package com.example.boardproject.api;

import com.example.boardproject.domain.auth.dto.request.LoginRequest;
import com.example.boardproject.domain.auth.dto.request.LogoutRequest;
import com.example.boardproject.domain.auth.dto.request.RefreshRequest;
import com.example.boardproject.domain.auth.dto.request.SignupRequest;
import com.example.boardproject.domain.auth.dto.response.SignupResponse;
import com.example.boardproject.domain.auth.dto.response.TokenResponse;
import com.example.boardproject.domain.auth.service.AuthService;
import com.example.boardproject.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(
            @Valid @RequestBody SignupRequest request) {

        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(ApiResponse.success(response, "회원가입이 완료되었습니다"));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "로그인이 완료되었습니다"));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody LogoutRequest request,
            @AuthenticationPrincipal UUID userId) {

        authService.logout(userId);
        return ResponseEntity.ok(ApiResponse.success("로그아웃이 완료되었습니다"));
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Valid @RequestBody RefreshRequest request) {

        TokenResponse response = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(response, "토큰이 재발급되었습니다"));
    }
}