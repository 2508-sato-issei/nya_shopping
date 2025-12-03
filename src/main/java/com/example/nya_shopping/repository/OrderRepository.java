package com.example.nya_shopping.repository;

import com.example.nya_shopping.controller.form.OrderNarrowForm;
import com.example.nya_shopping.repository.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface OrderRepository {
    List<Order> findOrder(@Param("form") OrderNarrowForm form,
                          @Param("offset") int offset,
                          @Param("limit") int limit);

    int countOrder(@Param("form") OrderNarrowForm form,
                   @Param("offset") int offset,
                   @Param("limit") int limit);

    int insert(Order order);

    Order findOrderById(@Param("id") String id);

    void updateOrderStatus(@Param("id") Integer id,
                           @Param("status") String status);
}
