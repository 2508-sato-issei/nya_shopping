package com.example.nya_shopping.repository.entity;

import com.example.nya_shopping.model.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private Category category;

    @Column
    private Integer stock;

    @Column
    private String imageUrl;

    @Column
    private String description;

    @Column
    private Boolean isActive;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;
}
