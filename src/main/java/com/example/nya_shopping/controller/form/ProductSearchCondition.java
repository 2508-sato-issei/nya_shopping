package com.example.nya_shopping.controller.form;

import lombok.Data;

@Data
public class ProductSearchCondition {

    private String name;
    private Integer priceMin;
    private Integer priceMax;

    private String category;

    private Boolean inStock;   // 在庫あり
    private Boolean isActive;  // 公開/非公開

    private String dateFrom;   // created_at >=
    private String dateTo;     // created_at <=

    private String sortKey;    // id, name, price, category, stock, created_at
    private String sortOrder;  // asc or desc

    private Integer page = 1;  // ページ番号
    private Integer size = 10; // 1ページ件数

    public int getOffset() {
        return (page - 1) * size;
    }
}