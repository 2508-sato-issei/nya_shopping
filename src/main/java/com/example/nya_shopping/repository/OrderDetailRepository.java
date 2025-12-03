package com.example.nya_shopping.repository;

import com.example.nya_shopping.repository.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailRepository {
    int insert(OrderDetail detail);
}
