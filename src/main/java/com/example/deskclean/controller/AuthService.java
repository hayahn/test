package com.example.deskclean.service;


import com.example.deskclean.security.JwtUtil;
import com.example.deskclean.dto.LoginRequestDto;
import com.example.deskclean.dto.SignupResponseDto; // SignupResponseDto import 추가
import com.example.deskclean.dto.TokenResponseDto;
import com.example.deskclean.entity.User;
import com.example.deskclean.exception.CustomException;
import com.example.deskclean.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.deskclean.dto.SignupRequestDto; // 새로 만든 DTO 임포트
import com.example.deskclean.entity.Role;         // Role enum 임포트
import jakarta.validation.Valid; // @Valid 임포트 확인
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    // ModelMapper 사용 안 하면 제거
    // private final ModelMapper modelMapper;

    public TokenResponseDto login(LoginRequestDto request) {
        // --- 수정: 이메일 기준으로 사용자 조회 ---
        User user = userRepository.findByEmail(request.getUsername()) // findByEmail 사용 (LoginRequestDto의 username이 이메일을 담는다고 가정)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "email", "사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // --- 수정: 비밀번호 오류 시 상태 코드 변경 ---
            throw new CustomException(HttpStatus.BAD_REQUEST, "password", "비밀번호가 일치하지 않습니다.");
        }

        // --- 수정: JWT 토큰 생성 시 이메일 사용 ---
        String accessToken = jwtUtil.createAccessToken(user.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        return new TokenResponseDto(accessToken, refreshToken);
    }

    // signup 메서드는 이전 답변 내용과 동일하게 유지
    public SignupResponseDto signup(@Valid SignupRequestDto request) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "email", "이미 사용 중인 이메일입니다.");
        }
        // 사용자 이름(닉네임) 중복 체크
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "username", "이미 사용 중인 사용자 이름입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .major(request.getMajor())
                .grade(request.getGrade())
                .school_number(request.getSchoolNumber())
                .gender(request.getGender())
                .role(Role.ROLE_USER) // 기본 역할 할당
                .build();

        userRepository.save(user);

        SignupResponseDto response = new SignupResponseDto();
        response.setUsername(user.getUsername());
        return response;
    }
}