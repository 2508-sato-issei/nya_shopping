package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.OrderNarrowForm;
import com.example.nya_shopping.repository.entity.Order;
import com.example.nya_shopping.service.OrderService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@MapperScan("com.example.nya_shopping.repository")
public class AdminOrdersController {
    @Autowired
    OrderService orderService;

    @GetMapping("/admin/orders")
    public String showAdminOrders(Model model,
                                  @ModelAttribute OrderNarrowForm form) {
        List<Order> order = orderService.findOrder(form);
        model.addAttribute("form", form);
        model.addAttribute("order", order);
        return "admin/orders";
    }
}
