package com.example.nya_shopping.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminDashboardRepository {

    int countToday();
    int countLastMonth();
    int countThisMonth();
    int countByStatus(@Param("status") String status);

}
