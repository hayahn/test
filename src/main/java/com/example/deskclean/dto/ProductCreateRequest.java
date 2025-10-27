package com.example.deskclean.dto;

import com.example.deskclean.entity.Category;
import com.example.deskclean.entity.Location;
import com.example.deskclean.entity.Product;
import com.example.deskclean.entity.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "카테고리는 필수 항목입니다.")
    private Category category;

    @NotBlank(message = "상품명은 필수 항목입니다.")
    private String productName;

    @NotBlank(message = "상품 설명은 필수 항목입니다.")
    private String productDescription;

    @NotNull(message = "상품 가격은 필수 항목입니다.")
    @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
    private Long productPrice;

    // 생성 시 기본 상태를 '판매중'으로 설정할 수 있으므로 Nullable하게 두거나,
    // 클라이언트에서 필수로 보내도록 @NotNull을 유지할 수 있습니다.
    @NotNull(message = "상품 상태는 필수 항목입니다.")
    private Status status;

    @NotNull(message = "거래 위치는 필수 항목입니다.")
    private Location location;

    // DTO를 Entity로 변환하는 메서드
    public Product toEntity() {
        return Product.builder()
                .category(this.category)
                .product_name(this.productName)
                .product_description(this.productDescription)
                .product_price(this.productPrice)
                .status(this.status)
                .location(this.location)
                .build();
    }
}