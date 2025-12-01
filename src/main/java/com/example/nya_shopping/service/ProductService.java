package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.form.SearchForm;
import com.example.nya_shopping.repository.ProductRepository;
import com.example.nya_shopping.repository.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.example.nya_shopping.controller.form.ProductForm;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import static com.example.nya_shopping.validation.ErrorMessage.E0028;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Page<Product> searchProduct(SearchForm form, PageRequest pageRequest) {

        int offset = (int) pageRequest.getOffset();
        int limit = pageRequest.getPageSize();

        // 検索結果を取得
        List<Product> products = productRepository.searchProducts(
                form.getCategory(),
                form.getKeyword(),
                form.getMinPrice(),
                form.getMaxPrice(),
                form.getStock(),
                form.getSort(),
                offset,
                limit
        );

        // 件数取得
        int total = productRepository.countProducts(
                form.getCategory(),
                form.getKeyword(),
                form.getMinPrice(),
                form.getMaxPrice(),
                form.getStock()
        );

        return new PageImpl<>(products, pageRequest, total);
    }

    @Autowired
    ProductRepository productRepository;

    public void create(ProductForm form) throws IOException {

        // 画像チェック（サイズ・拡張子）
        MultipartFile file = form.getImageFile();
        if (file.isEmpty()) {
            throw new RuntimeException(E0028);
        }

        // UUIDファイル名生成
        String ext = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + ext;

        // 保存先
        Path uploadPath = Paths.get("uploads/products/" + fileName);
        Files.copy(file.getInputStream(), uploadPath);

        // Entityへ変換して保存
        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setStock(form.getStock());
        product.setCategory(form.getCategory());
        product.setDescription(form.getDescription());
        product.setImageUrl("/images/products/" + fileName);

        productRepository.insert(product);
    }

}
