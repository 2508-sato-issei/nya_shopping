package com.example.nya_shopping.controller;

import com.example.nya_shopping.dto.DashboardSummaryDto;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    DashboardService dashboardService;

    /* 管理者画面表示 */
    @GetMapping
    public String showDashboard(Model model) {

        DashboardSummaryDto summary = dashboardService.getSummary();

        List<Category> popularCategories = List.of(
                Category.PC_AND_PERIPHERALS,
                Category.STATIONERY_AND_SUPPLIES,
                Category.OA_EQUIPMENT);

        List<String> popularKeywords = List.of(
                "ノートPC", "プリンタ", "オフィスチェア", "業務用");

        model.addAttribute("summary", summary);
        model.addAttribute("popularCategories", popularCategories);
        model.addAttribute("popularKeywords", popularKeywords);

        return "admin/dashboard";
    }
}
