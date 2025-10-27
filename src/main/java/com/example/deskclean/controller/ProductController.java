package com.example.deskclean.controller;

import com.example.deskclean.dto.ProductCreateRequest;
import com.example.deskclean.dto.ProductResponse;
import com.example.deskclean.dto.ProductUpdateRequest;
import com.example.deskclean.entity.Product;
import com.example.deskclean.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    // Create
    @PostMapping
    // 권한 설정
    // 우선 유저만 상품 등록 가능하게 설정함
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductCreateRequest request) {
        Product savedProduct = productService.save(request);
        ProductResponse response = ProductResponse.fromEntity(savedProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Read
    // 전체 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductResponse>> viewAllProduct() {
        List<Product> products = productService.findAll();
        List<ProductResponse> responses = products.stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // 특정 상품 아이디 검색 조회
    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> viewProduct(@PathVariable Long id) {
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(ProductResponse.fromEntity(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 페이징 처리
    @GetMapping("/paging")
    public ResponseEntity<Page<ProductResponse>> viewPagingProduct(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {

        Page<Product> productsPage = productService.findPageable(pageable);
        Page<ProductResponse> responses = productsPage.map(ProductResponse::fromEntity);
        return ResponseEntity.ok(responses);
    }

    // Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') and @authChecker.checkProductOwner(#id, principal.username)")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        return productService.update(id, request)
                .map(updatedProduct -> ResponseEntity.ok(ProductResponse.fromEntity(updatedProduct)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete (soft delete mapping)
    @PutMapping("/{id}/soft")
    @PreAuthorize("hasRole('ROLE_USER') and @authChecker.checkProductOwner(#id, principal.username)")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        boolean isDeleted = productService.softDelete(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

// TODO
// Custom Exception 처리