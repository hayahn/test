package com.example.deskclean.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// TODO
// 유저별 상세페이지 방문 기록 저장
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class ViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime firstViewedAt;

    @UpdateTimestamp // 엔티티가 업데이트될 때마다 시간 자동 갱신
    private LocalDateTime lastViewedAt;

    // setter
    // 마지막 방문 update 하는 setter
    public void updateLastViewedAt() {
        this.lastViewedAt = LocalDateTime.now();
    }

    @Builder
    public ViewHistory(User user, Product product) {
        this.user = user;
        this.product = product;
    }
}