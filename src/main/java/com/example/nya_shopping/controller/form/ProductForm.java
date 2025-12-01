package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ProductForm {
    private Integer id;
    private String name;
    private Integer price;
    private String category;
    private Integer stock;
    private String imageUrl;
    private String description;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
