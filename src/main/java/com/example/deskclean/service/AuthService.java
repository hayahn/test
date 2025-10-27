package com.example.deskclean.service;


import com.example.deskclean.security.JwtUtil;
import com.example.deskclean.dto.LoginRequestDto;
import com.example.deskclean.dto.SignupResponseDto;
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
    private final ModelMapper modelMapper; // ModelMapper를 사용하지 않는다면 제거 가능

    public TokenResponseDto login(LoginRequestDto request) {
        // 로그인 시 username 필드가 실제로는 이메일을 받는다고 가정하고 수정
        User user = userRepository.findByEmail(request.getUsername()) // findByEmail 사용
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "email", "사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 비밀번호 불일치 시 400 Bad Request 또는 401 Unauthorized가 더 적절할 수 있음
            throw new CustomException(HttpStatus.BAD_REQUEST, "password", "비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 시 이메일 사용
        String accessToken = jwtUtil.createAccessToken(user.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        return new TokenResponseDto(accessToken, refreshToken);
    }

    // signup 메서드가 SignupRequestDto를 받도록 수정
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
                .email(request.getEmail()) // DTO에서 email 사용
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername()) // DTO에서 username(닉네임) 사용
                .major(request.getMajor())
                .grade(request.getGrade())
                .school_number(request.getSchoolNumber()) // DTO 필드 이름과 맞춤 (schoolNumber)
                .gender(request.getGender())
                .role(Role.ROLE_USER) // 기본 역할 할당
                .build();

        userRepository.save(user);

        // 응답 DTO 생성 (ModelMapper 대신 직접 생성)
        SignupResponseDto response = new SignupResponseDto();
        response.setUsername(user.getUsername()); // 가입된 사용자 이름(닉네임) 반환
        // 필요하다면 다른 정보도 포함 가능
        return response;
    }
}