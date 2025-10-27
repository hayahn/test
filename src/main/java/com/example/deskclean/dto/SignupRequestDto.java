// src/main/java/com/example/deskclean/dto/SignupRequestDto.java
package com.example.deskclean.dto;

import com.example.deskclean.entity.Gender; // Gender enum 임포트
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.") // 이메일 형식 검증 추가
    private String email; // User 엔티티의 'email' 필드와 매칭

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @NotBlank(message = "사용자 이름(닉네임)은 필수 항목입니다.")
    private String username; // User 엔티티의 'username' 필드와 매칭

    @NotBlank(message = "전공은 필수 항목입니다.")
    private String major;

    @NotBlank(message = "학년은 필수 항목입니다.")
    private String grade;

    @NotBlank(message = "학번은 필수 항목입니다.")
    private String schoolNumber; // JSON 요청 필드 이름과 맞추기 (school_number -> schoolNumber)

    @NotNull(message = "성별은 필수 항목입니다.") // 성별이 필수라고 가정
    private Gender gender;

    // Role(역할)은 보통 클라이언트가 보내는 것이 아니라 서버에서 지정합니다.
}