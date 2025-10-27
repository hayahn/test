package com.example.deskclean.security;

import java.io.IOException;
// import java.util.Collections; // 제거
import java.util.Optional; // Optional 임포트

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // UserDetails 임포트
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.deskclean.entity.User;
import com.example.deskclean.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.isValidToken(token) && !jwtUtil.isTokenExpired(token)) {
                // --- 수정: extractUsername이 실제로는 이메일을 반환 ---
                String email = jwtUtil.extractUsername(token);

                // --- 수정: 이메일로 사용자 조회 ---
                Optional<User> userOptional = userRepository.findByEmail(email); // findByEmail 사용

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    // --- 수정: CustomUserDetails 사용 및 권한 설정 ---
                    UserDetails userDetails = new CustomUserDetails(user); // CustomUserDetails 생성
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, // Principal로 UserDetails 사용
                                    null,        // Credentials는 필요 없음 (JWT 사용)
                                    userDetails.getAuthorities() // 권한 정보 전달
                            );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}