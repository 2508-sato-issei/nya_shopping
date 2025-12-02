package com.example.nya_shopping.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
    private String category;
    private String imageUrl;
}