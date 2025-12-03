package com.example.nya_shopping.dto;

import lombok.Data;

@Data
public class OrderDetailDto {
    //リスト型を想定
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private Integer productPrice;
    private Integer quantity;
    private Integer subtotal;
    private String imageUrl;
}
