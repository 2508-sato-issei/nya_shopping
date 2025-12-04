package com.example.nya_shopping.repository;

import com.example.nya_shopping.repository.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepository {
    Optional<User> findByEmail(@Param("email") String email);
    void save(User user);
}
