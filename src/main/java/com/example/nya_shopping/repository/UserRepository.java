package com.example.nya_shopping.repository;

import com.example.nya_shopping.repository.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
}
