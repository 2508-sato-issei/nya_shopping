package com.example.nya_shopping.controller.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductSearchCondition {

    private String name;          // 商品名部分一致
    private Integer priceMin;     // 価格下限
    private Integer priceMax;     // 価格上限
    private String category;      // カテゴリ
    private Boolean stockOnly;    // 在庫ありのみ
    private Boolean isActive;     // 公開・非公開
    private String dateFrom;      // 登録日時（開始）
    private String dateTo;        // 登録日時（終了）

    // ソート項目（id, name, price, category, stock, created_at）
    private String sortKey;

    // ソート順（asc / desc）
    private String sortOrder;
}