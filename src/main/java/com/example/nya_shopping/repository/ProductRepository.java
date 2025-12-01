package com.example.nya_shopping.repository;

import com.example.nya_shopping.repository.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductRepository {

    void insert(Product product);

}
