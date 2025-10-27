package com.example.deskclean.dto;

import com.example.deskclean.entity.Category;
import com.example.deskclean.entity.Location;
import com.example.deskclean.entity.Product;
import com.example.deskclean.entity.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductResponse {

    private final Long id;
    private final Category category;
    private final String productName;
    private final String productDescription;
    private final Long productPrice;
    private final Status status;
    private final Location location;
    private final int viewCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public ProductResponse(Long id, Category category, String productName, String productDescription, Long productPrice, Status status, Location location, int viewCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.category = category;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.status = status;
        this.location = location;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Entity를 DTO로 변환하는 정적 팩토리 메서드
    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .category(product.getCategory())
                .productName(product.getProduct_name())
                .productDescription(product.getProduct_description())
                .productPrice(product.getProduct_price())
                .status(product.getStatus())
                .location(product.getLocation())
                .viewCount(product.getView_count())
                .createdAt(product.getCreated_at())
                .updatedAt(product.getUpdated_at())
                .build();
    }
}