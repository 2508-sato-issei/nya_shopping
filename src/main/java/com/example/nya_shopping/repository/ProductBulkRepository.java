package com.example.nya_shopping.repository;

import com.example.nya_shopping.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface ProductBulkRepository {

    int exists(@Param("id") Integer id);

    void insertProduct(
            @Param("name") String name,
            @Param("price") Integer price,
            @Param("category") Category category,
            @Param("stock") Integer stock,
            @Param("imageUrl") String imageUrl,
            @Param("description") String description,
            @Param("isActive") Boolean isActive,
            @Param("createdAt") LocalDateTime createdAt);

    void updateProduct(
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("price") Integer price,
            @Param("category") Category category,
            @Param("stock") Integer stock,
            @Param("imageUrl") String imageUrl,
            @Param("description") String description,
            @Param("isActive") Boolean isActive,
            @Param("createdAt") LocalDateTime createdAt);
}
