package com.example.nya_shopping.dto;

import com.example.nya_shopping.repository.entity.Product;

//カートの情報を表示するためのDTO
public record CartView(Product product, Integer quantity) {}
