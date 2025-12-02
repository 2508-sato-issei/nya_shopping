package com.example.nya_shopping.converter;

import com.example.nya_shopping.controller.form.ProductForm;
import com.example.nya_shopping.repository.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    /* Entity → Form */
    public ProductForm toForm(Product product) {
        ProductForm form = new ProductForm();
        form.setId(product.getId());
        form.setName(product.getName());
        form.setPrice(product.getPrice());
        form.setCategory(product.getCategory());
        form.setStock(product.getStock());
        form.setImageUrl(product.getImageUrl());
        form.setDescription(product.getDescription());
        form.setIsActive(product.getIsActive());
        form.setCreatedAt(product.getCreatedAt());
        form.setUpdatedAt(product.getUpdatedAt());
        return form;
    }

}
