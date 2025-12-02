package com.example.nya_shopping.controller;

import com.example.nya_shopping.repository.entity.Product;
import com.example.nya_shopping.service.OrderDetailService;
import com.example.nya_shopping.service.OrderService;
import com.example.nya_shopping.service.PaymentService;
import com.example.nya_shopping.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PurchaseController {

    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    PaymentService paymentService;

    // カート画面を開く
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {

        List<Integer> cart = (List<Integer>) session.getAttribute("cart");
        if(cart == null){
            cart = new ArrayList<>();
        }

        List<Product> items = new ArrayList<>();
        for(Integer id  : cart){
            Product product = productService.findById(id);
            items.add(product);
        }

        model.addAttribute("items", items);
        return "cart";
    }

    //カートに追加する処理
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("id") Integer productId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // セッションからカート取得（なければ生成）
        List<Integer> cart = (List<Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        cart.add(productId);
        session.setAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("message", "カートに追加しました");
        return "redirect:/cart";
    }
}
