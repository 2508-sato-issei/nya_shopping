package com.example.nya_shopping.repository;

import com.example.nya_shopping.controller.form.OrderNarrowForm;
import com.example.nya_shopping.repository.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderRepository {
    List<Order> findOrder(OrderNarrowForm form);
}
