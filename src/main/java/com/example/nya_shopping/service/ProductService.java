package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.form.SearchForm;
import com.example.nya_shopping.repository.ProductRepository;
import com.example.nya_shopping.repository.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public Product findById(Integer id) {
        return productRepository.findById(id);
    }
}
