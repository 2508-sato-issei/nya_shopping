package com.example.nya_shopping.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer orderId;

    @Column
    private String stripeSessionId;

    @Column
    private String stripePaymentIntent;

    @Column
    private Integer amount;

    @Column
    private String currency;

    @Column
    private String status;

    @Column
    private Timestamp paidAt;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;
}
