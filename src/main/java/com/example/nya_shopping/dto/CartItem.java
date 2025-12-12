package com.example.nya_shopping.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private Integer productId;
    private Integer quantity;

    public CartItem() {
        // 空のコンストラクタ（Spring がセッション復元に使う）
    }

    //最初から完成された箱を作っておく
    public CartItem(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
