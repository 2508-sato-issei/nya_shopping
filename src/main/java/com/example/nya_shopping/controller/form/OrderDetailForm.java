package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OrderDetailForm {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private Integer productPrice;
    private Integer quantity;
    private Integer subtotal;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
