package com.example.nya_shopping.dto;

import com.example.nya_shopping.model.Category;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private Integer price;
    private Category category;
    private Integer stock;
    private Boolean isActive;
    private Timestamp createdAt;
}