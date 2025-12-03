package com.example.nya_shopping.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductBulkRepository {

    int exists(@Param("id") Long id);

    void insertProduct(
            @Param("name") String name,
            @Param("price") Integer price,
            @Param("category") String category,
            @Param("stock") Integer stock,
            @Param("imageUrl") String imageUrl,
            @Param("description") String description,
            @Param("isActive") Boolean isActive);

    void updateProduct(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("price") Integer price,
            @Param("category") String category,
            @Param("stock") Integer stock,
            @Param("imageUrl") String imageUrl,
            @Param("description") String description,
            @Param("isActive") Boolean isActive);
}
