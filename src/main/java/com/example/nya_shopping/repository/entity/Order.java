package com.example.nya_shopping.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer userId;

    @Column
    private Integer totalAmount;

    @Column
    private String customerName;

    @Column
    private String customerPostalCode;

    @Column
    private String customerAddress;

    @Column
    private String customerPhone;

    @Column
    private String status;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;
}
