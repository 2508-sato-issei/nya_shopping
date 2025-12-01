package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchForm {

    private String category;
    private String keyword;
    private Integer maxPrice;
    private Integer minPrice;
    private Integer stock;
    private String sort;
}
