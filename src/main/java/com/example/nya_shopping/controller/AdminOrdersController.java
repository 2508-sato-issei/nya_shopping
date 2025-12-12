package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.OrderNarrowForm;
import com.example.nya_shopping.controller.form.OrderStatusForm;
import com.example.nya_shopping.dto.OrderDetailDto;
import com.example.nya_shopping.repository.entity.Order;
import com.example.nya_shopping.repository.entity.Product;
import com.example.nya_shopping.service.OrderDetailService;
import com.example.nya_shopping.service.OrderService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.nya_shopping.validation.ErrorMessage.E0001;
import static com.example.nya_shopping.validation.ErrorMessage.E0018;

@Controller
@MapperScan("com.example.nya_shopping.repository")
public class AdminOrdersController {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

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
    @GetMapping("admin/order/{id}")
    public String showAdminOrder(@PathVariable String id, Model model) {

        //IDチェック
        if (!id.matches("\\d+")) {
            model.addAttribute("errorMessages", E0018);
            return "admin/orders";
        }

        Order order = orderService.findOrderById(id);

        // OrderStatusForm を自分で生成してセット
        OrderStatusForm form = new OrderStatusForm();
        form.setId(order.getId());
        form.setStatus(order.getStatus());

        List<OrderDetailDto> orderDetailDtoList =
                orderDetailService.findOrderDetailById(order.getId());

        model.addAttribute("order", order);
        model.addAttribute("orderDetailDtoList", orderDetailDtoList);
        model.addAttribute("orderStatusForm", form);

        return "admin/order";
    }


    //注文管理詳細画面 IDがない場合の処理
    @GetMapping("admin/order")
    public String adminOrderError(Model model) {
        model.addAttribute("errorMessages", E0018);
        return "admin/orders";
    }

    //注文管理ステータス変更
    @PostMapping("admin/order/status")
    public String adminOrderStatus(Model model,
                                   @ModelAttribute OrderStatusForm form) {
        orderService.updateOrderStatus(form);
        return "redirect:/admin/orders";
    }

}
