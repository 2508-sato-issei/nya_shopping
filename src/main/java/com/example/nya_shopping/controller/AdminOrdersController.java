package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.OrderNarrowForm;
import com.example.nya_shopping.repository.entity.Order;
import com.example.nya_shopping.repository.entity.Product;
import com.example.nya_shopping.service.OrderService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@MapperScan("com.example.nya_shopping.repository")
public class AdminOrdersController {
    @Autowired
    OrderService orderService;

    //注文管理一覧画面
    @GetMapping("/admin/orders")
    public String showAdminOrders(Model model,
                                  @ModelAttribute OrderNarrowForm form,
                                  @RequestParam(defaultValue = "0") int page) {
        //List<Order> order = orderService.findOrder(form);

        Page<Order> resultPage = orderService.findOrder(form, PageRequest.of(page, 10));
        int totalPages = resultPage.getTotalPages();
        int currentPage = resultPage.getNumber();
        int displayRange = 5;
        int startPage = 0;
        int endPage = 0;

        if (totalPages > 0) {
            startPage = Math.max(0, currentPage - displayRange);
            endPage = Math.min(totalPages - 1, currentPage + displayRange);
        }

        boolean showFirst = totalPages > 0 && startPage > 0;
        boolean showLast = totalPages > 0 && endPage < totalPages - 1;

        model.addAttribute("form", form);
        model.addAttribute("order", resultPage);

        // ページネーション
        model.addAttribute("page", resultPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("showFirst", showFirst);
        model.addAttribute("showLast", showLast);
        return "admin/orders";
    }

    //注文管理詳細画面
//    @GetMapping("admin/order/{id}")
//    public String showAdminOrder(@PathVariable Integer id, Model model) {
//        Order order = orderService.find
//    }
}
