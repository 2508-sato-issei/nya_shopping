package com.example.nya_shopping.controller;

import com.example.nya_shopping.dto.CartItem;
import com.example.nya_shopping.dto.CartView;
import com.example.nya_shopping.model.Category;
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

    // カート画面を開く(「Spring Boot が動かしている組み込みTomcatの “サーバー側メモリ領域”」 にセッションを保存)
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {

        //Tomcat のセッションストレージ（メモリ）から cart を取り出す
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if(cart == null){
            cart = new ArrayList<>();
        }

        //表示用に変換
        List<CartView> items = new ArrayList<>();
        for (CartItem ci : cart) {
            Product product = productService.findById(ci.getProductId());
            items.add(new CartView(product, ci.getQuantity()));
        }

        model.addAttribute("items", items);
        model.addAttribute("categories", Category.values());
        return "/user/cart";
    }

    //カートに追加する処理
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("id") Integer productId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // セッションからカート取得（なければ生成）
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {cart = new ArrayList<>();}
        boolean exists = false;
        for(CartItem item : cart){
            if(item.getProductId().equals(productId)){
                item.setQuantity(item.getQuantity() + 1);
                exists = true;
                break;
            }
        }

        //CartItem を一発で正しい状態に初期化するためにコンストラクタが必要
        if(!exists){
            cart.add(new CartItem(productId, 1));
        }

        session.setAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("message", "カートに追加しました");
        return "redirect:/cart";
    }

    //カート個数変更処理
    @PostMapping("/cart/update")
    public String updateProduct(@RequestParam("productId") Integer productId,
                                @RequestParam("quantity") Integer quantity,
                                HttpSession session,
                                RedirectAttributes redirectAttributes){

        //最初にセッションからカート情報を取得
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart"));

    }
}
