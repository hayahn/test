// src/main/java/com/example/deskclean/dto/SignupResponseDto.java
package com.example.deskclean.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {
    private String username; // 회원가입된 사용자의 닉네임
    // 필요하다면 email 등 다른 필드 추가 가능
}