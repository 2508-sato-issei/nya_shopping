package com.example.nya_shopping.dto;

import com.example.nya_shopping.model.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDto {
    private Integer id;
    private String name;
    private Integer price;
    private Category category;
    private Integer stock;
    private String imageUrl;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
}