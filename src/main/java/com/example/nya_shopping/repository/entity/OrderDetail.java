package com.example.nya_shopping.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "order_details")
@Getter
@Setter
public class OrderDetail {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer orderId;

    @Column
    private Integer productId;

    @Column
    private String productName;

    @Column
    private Integer productPrice;

    @Column
    private Integer quantity;

    @Column
    private Integer subtotal;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;
}
