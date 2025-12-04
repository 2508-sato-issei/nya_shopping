package com.example.nya_shopping.service;

import com.example.nya_shopping.dto.DashboardSummaryDto;
import com.example.nya_shopping.repository.AdminDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    AdminDashboardRepository adminDashboardRepository;

    public DashboardSummaryDto getSummary() {

        DashboardSummaryDto dto = new DashboardSummaryDto();

        dto.setTodayOrders(adminDashboardRepository.countToday());
        dto.setLastMonthOrders(adminDashboardRepository.countLastMonth());
        dto.setThisMonthOrders(adminDashboardRepository.countThisMonth());

        dto.setConfirmed(adminDashboardRepository.countByStatus("CONFIRMED"));
        dto.setPreparing(adminDashboardRepository.countByStatus("PREPARING"));
        dto.setShipped(adminDashboardRepository.countByStatus("SHIPPED"));

        return dto;
    }
}
