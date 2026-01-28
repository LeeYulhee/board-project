package com.example.boardproject.api;

import com.example.boardproject.api.in.AuthApi;
import com.example.boardproject.domain.auth.dto.request.LoginRequest;
import com.example.boardproject.domain.auth.dto.request.SignupRequest;
import com.example.boardproject.domain.auth.dto.response.SignupResponse;
import com.example.boardproject.domain.auth.dto.response.TokenResponse;
import com.example.boardproject.domain.auth.service.AuthService;
import com.example.boardproject.global.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<SignupResponse>> signup(
            @Valid @RequestBody SignupRequest request) {

        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(response, "회원가입이 완료되었습니다"));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(SuccessResponse.of(response, "로그인이 완료되었습니다"));
    }
}