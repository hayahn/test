package com.example.deskclean.service;

import com.example.deskclean.dto.ProductCreateRequest;
import com.example.deskclean.dto.ProductUpdateRequest;
import com.example.deskclean.entity.Product;
import com.example.deskclean.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    // Create
    @Transactional
    public Product save(ProductCreateRequest request) {
        Product product = request.toEntity();
        return productRepository.save(product);
    }

    // Read
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> findPageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Update
    @Transactional
    public Optional<Product> update(Long id, ProductUpdateRequest updateRequest) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setCategory(updateRequest.getCategory());
                    product.setProduct_name(updateRequest.getProductName());
                    product.setProduct_description(updateRequest.getProductDescription());
                    product.setProduct_price(updateRequest.getProductPrice());
                    product.setStatus(updateRequest.getStatus());
                    product.setLocation(updateRequest.getLocation());
                    return productRepository.save(product);
                });
    }

    // Delete (soft delete)
    @Transactional
    public boolean softDelete(Long id){
        return productRepository.findById(id)
                .map(product -> {
                    product.set_deleted(true);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }
}