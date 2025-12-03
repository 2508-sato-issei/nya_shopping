package com.example.nya_shopping.repository;

import com.example.nya_shopping.controller.form.ProductSearchCondition;
import com.example.nya_shopping.dto.ProductDto;
import com.example.nya_shopping.repository.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

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

    Product findByIdIsActive(Integer id);

    /* 商品管理一覧画面表示 */
    List<ProductDto> search(@Param("cond") ProductSearchCondition cond);

    int count(@Param("cond") ProductSearchCondition cond);

    List<ProductDto> searchAll(@Param("cond") ProductSearchCondition cond); // CSV用

    /* 商品登録処理 */
    void insert(Product product);

    /* 商品編集画面表示 */
    Optional<Product> FindById(Long id);

    /* 商品編集処理 */
    void update(Product product);

}
