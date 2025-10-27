package com.example.deskclean.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Valid

@Table(name = "users") // DB 예약어 방지
public class User {

    // 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // 학교 이메일
    @Column(unique = true, nullable = false)
    private String email;

    // 패스워드
    @Column(nullable = false)
    private String password;

    // 유저 닉네임
    @Column(unique = true, nullable = false)
    private String username;

    // 전공
    @Column(nullable = false)
    private String major;

    // 학년
    @Column(nullable = false)
    private String grade;

    // 학번
    @Column(nullable = false)
    private String school_number;

    // 성별
    @Enumerated(EnumType.STRING)
    private Gender gender;

    // Role
    @Enumerated(EnumType.STRING)
    private Role role;
}