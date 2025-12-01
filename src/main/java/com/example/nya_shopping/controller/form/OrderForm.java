package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OrderForm {
    private Integer id;
    private Integer userId;
    private Integer totalAmount;
    private String customerName;
    private String customerPostalCode;
    private String customerAddress;
    private String customerPhone;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
