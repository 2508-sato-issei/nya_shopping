package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.PurchaseForm;
import com.example.nya_shopping.dto.CartItem;
import com.example.nya_shopping.dto.CartView;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.repository.entity.Product;
import com.example.nya_shopping.service.OrderDetailService;
import com.example.nya_shopping.service.OrderService;
import com.example.nya_shopping.service.PaymentService;
import com.example.nya_shopping.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

import static com.example.nya_shopping.validation.ErrorMessage.E0019;

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

    //購入者情報入力画面を表示する
    @GetMapping("/order/purchase")
    public String inputPurchase(HttpSession session, Model model){

        //セッションからカートを取得
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if(cart == null || cart.isEmpty()){
            model.addAttribute("error", "カートが空です");
            return "redirect:/cart";
        }

        // 在庫チェック追加
        for (CartItem ci : cart) {
            Product p = productService.findById(ci.getProductId());

            if (p.getStock() < ci.getQuantity()) {
                model.addAttribute("errorMessage", E0019);
                return "forward:/cart";
            }
        }

        List<CartView> items = new ArrayList<>();
        int totalAmount = 0;

        for(CartItem ci : cart){
            Product product = productService.findById(ci.getProductId());
            Integer quantity = ci.getQuantity();
            //合計金額を算出
            int price = product.getPrice();
            int subtotal = price * quantity;
            totalAmount = totalAmount + subtotal;
            items.add(new CartView(product,quantity));
        }

        if(!model.containsAttribute("purchaseForm")){
            model.addAttribute("purchaseForm", new PurchaseForm());
        }

        model.addAttribute("items", items);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("categories", Category.values());
        return "/user/purchase";
    }

    //購入者情報入力処理（バリデーションあり）
    @PostMapping("/order/purchase/confirm")
    public String productPurchaser(@Valid @ModelAttribute("purchaseForm") PurchaseForm purchaseForm,
                                   BindingResult result, HttpSession session, Model model){
        if(result.hasErrors()){
            //カート情報を取得
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            List<CartView> items = new ArrayList<>();
            int totalAmount = 0;

            for(CartItem ci : cart){
                Product product = productService.findById(ci.getProductId());
                Integer quantity = ci.getQuantity();
                //合計金額を算出
                int price = product.getPrice();
                int subtotal = price * quantity;
                totalAmount = totalAmount + subtotal;
                items.add(new CartView(product,quantity));
            }

            model.addAttribute("items", items);
            model.addAttribute("totalAmount", totalAmount);
            return "/user/purchase";
        }

        session.setAttribute("purchaseForm", purchaseForm);
        return "redirect:/order/purchase/payment";
    }
}
