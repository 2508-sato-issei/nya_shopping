package com.example.nya_shopping.dto;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

//合計購入点数を補完するためのもの
@Getter
@Setter
public class OrderHistoryItemDto {
    private Integer id;
    private Integer totalAmount;
    private String status;
    private Timestamp createdAt;
    //サービスで計算し、ここに格納する
    private Integer totalItems;
}
