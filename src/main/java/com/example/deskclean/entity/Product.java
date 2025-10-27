package com.example.deskclean.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// validation
@Valid

// 리스너 설정
@EntityListeners(AuditingEntityListener.class)
public class Product {

    // 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    // 카테고리 선택 (enum 타입)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    // 상품명
    @NotNull
    private String product_name;

    // 상품 설명
    @NotNull
    private String product_description;

    // 상품 가격
    @NotNull
    private Long product_price;

    // 상품 상태 (enum 타입)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    // 생성일시
    @CreatedDate
    private LocalDateTime created_at;

    // 수정 일시
    @LastModifiedDate
    private LocalDateTime updated_at;

    // 삭제 여부
    // soft delete
    private boolean is_deleted;

    // 조회수
    private int view_count;

    // 거래 희망 위치 (enum 타입)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Location location;

    // 상품, 유저 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
