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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public TokenResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "username", "User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(HttpStatus.NOT_FOUND, "username","Invalid password");
        }

        String accessToken = jwtUtil.createAccessToken(user.getUsername());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

        return new TokenResponseDto(accessToken, refreshToken);
    }

    public SignupResponseDto signup(LoginRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "username", "Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        return modelMapper.map(user, SignupResponseDto.class);
    }
}