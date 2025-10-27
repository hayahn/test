package com.example.deskclean.controller;

import com.example.deskclean.dto.LoginRequestDto;
import com.example.deskclean.dto.SignupRequestDto;
import com.example.deskclean.dto.SignupResponseDto;
import com.example.deskclean.dto.TokenResponseDto;
import com.example.deskclean.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.deskclean.dto.SignupRequestDto; // 새로 만든 DTO 임포트
import jakarta.validation.Valid; // @Valid 임포트 확인


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto request) {
        TokenResponseDto tokenResponse = authService.login(request);
        return ResponseEntity.ok(tokenResponse); // 200 OK
    }

   @PostMapping("/signup")
    // 받는 타입을 SignupRequestDto로 변경하고 @Valid 추가
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto request) {
        SignupResponseDto signupResponse = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponse);
    }
}

