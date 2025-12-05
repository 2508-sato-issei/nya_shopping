package com.example.nya_shopping.repository;

import com.example.nya_shopping.controller.form.ProductSearchCondition;
import com.example.nya_shopping.dto.ProductDto;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.repository.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;

import java.net.ContentHandler;
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
    void insertProduct(
            @Param("name") String name,
            @Param("price") Integer price,
            @Param("category") Category category,
            @Param("stock") Integer stock,
            @Param("imageUrl") String imageUrl,
            @Param("description") String description,
            @Param("isActive") Boolean isActive
    );

    Optional<ProductDto> findById(@Param("id") Long id);

    void update(
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("price") Integer price,
            @Param("category") Category category,
            @Param("stock") Integer stock,
            @Param("imageUrl") String imageUrl,
            @Param("description") String description,
            @Param("isActive") Boolean isActive
    );

    void delete(@Param("id") Long id);

    //在庫を減らす際に必要な処理
    Product findProductById(Integer id);
    void updateStock(Product product);

    List<Product> findRecentProducts(@Param("limit") int limit);
}
