package com.example.nya_shopping.repository;

import com.example.nya_shopping.controller.form.MyPageOrderNarrowForm;
import com.example.nya_shopping.controller.form.OrderNarrowForm;
import com.example.nya_shopping.dto.OrderHistoryItemDto;
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

    //マイページで注文履歴一覧を取得する。
    List<OrderHistoryItemDto> findOrderHistory(@Param("form") MyPageOrderNarrowForm form);

    //注文した年を取得
    List<Integer> findOrderYearsByUserId(@Param("userId") Integer userId);



}
