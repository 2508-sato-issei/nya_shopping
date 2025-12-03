package com.example.nya_shopping.controller;

import com.example.nya_shopping.dto.DashboardSummaryDto;
import com.example.nya_shopping.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    DashboardService dashboardService;

    /* 管理者画面表示 */
    @GetMapping
    public String showDashboard(Model model) {

        DashboardSummaryDto summary = dashboardService.getSummary();

        model.addAttribute("summary", summary);

        return "admin/dashboard";
    }
}
