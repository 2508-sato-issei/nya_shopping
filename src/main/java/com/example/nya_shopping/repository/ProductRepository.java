package com.example.nya_shopping.repository;

import com.example.nya_shopping.repository.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ProductRepository {

    List<Product> searchProducts(
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("stock") Integer stock,
            @Param("sort") String sort,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
    int countProducts(
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("stock") Integer stock
    );

    /* 商品登録 */
    void insert(Product product);


    Product findById(Integer id);
}
