// src/main/java/com/example/deskclean/repository/UserRepository.java
package com.example.deskclean.repository;

import com.example.deskclean.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // 이메일로 찾는 메서드 추가
}