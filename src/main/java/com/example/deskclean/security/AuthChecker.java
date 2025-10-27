package com.example.deskclean.security;
import org.springframework.stereotype.Component;

import com.example.deskclean.repository.ProductRepository;

@Component("authChecker")
public class AuthChecker {

    private final ProductRepository productRepository;

    public AuthChecker(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean checkProductOwner(Long productId, String username) {
        return productRepository.findById(productId)
                .map(product -> product.getUser().getUsername().equals(username))
                .orElse(false);
    }

}