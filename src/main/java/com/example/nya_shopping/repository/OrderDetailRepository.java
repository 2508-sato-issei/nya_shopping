package com.example.nya_shopping.repository;

import com.example.nya_shopping.dto.OrderDetailDto;
import com.example.nya_shopping.repository.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface OrderDetailRepository {
    int insert(OrderDetail detail);
    List<OrderDetailDto> findOrderDetailList(@Param("id") int id);
}
