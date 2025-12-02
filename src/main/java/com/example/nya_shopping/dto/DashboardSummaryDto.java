package com.example.nya_shopping.dto;

import lombok.Data;

@Data
public class DashboardSummaryDto {
    private int todayOrders;
    private int lastMonthOrders;
    private int thisMonthOrders;

    private int confirmed;
    private int preparing;
    private int shipped;
}
