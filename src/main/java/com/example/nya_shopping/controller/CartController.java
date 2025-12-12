package com.example.nya_shopping.controller;

import com.example.nya_shopping.dto.CartItem;
import com.example.nya_shopping.dto.CartView;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.repository.entity.Product;
import com.example.nya_shopping.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    ProductService productService;

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
        return "cart";
    }

    //カートに追加する処理
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("id") Integer productId,
                            @RequestParam("quantity") Integer quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // セッションからカート取得（なければ生成）
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {cart = new ArrayList<>();}
        boolean exists = false;
        for(CartItem item : cart){
            if(item.getProductId().equals(productId)){
                item.setQuantity(item.getQuantity() + quantity);
                exists = true;
                break;
            }
        }

        //CartItem を一発で正しい状態に初期化するためにコンストラクタが必要
        if(!exists){
            cart.add(new CartItem(productId, quantity));
        }

        session.setAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("message", "カートに追加しました");
        return "redirect:/cart";
    }

    //カート個数変更処理
    @PostMapping("/cart/update")
    @ResponseBody
    //数量変更フォームから送られてくる商品IDと数量を受け取る
    public String updateProduct(@RequestParam("productId") Integer productId,
                                @RequestParam("quantity") Integer quantity,
                                HttpSession session){

        //最初にセッションからカート情報を取得
        List<CartItem> cart = ((List<CartItem>) session.getAttribute("cart"));
        if(cart == null){
            return "ERROR:カートが空です";
        }

        //最新の在庫数を確認するために商品情報を取得
        Product product = productService.findById(productId);
        if(product ==  null){
            return "ERROR:商品が存在しません";
        }

        //カート数量更新処理（セッション内の情報を更新）
        for(CartItem item : cart){
            if(item.getProductId().equals(productId)){
                item.setQuantity(quantity);
                break;
            }
        }

        session.setAttribute("cart", cart);
        return "OK";
    }

    //カートの商品を削除する処理
    @PostMapping("/cart/delete")
    public String deleteProduct(@RequestParam("productId") Integer productId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes){

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if(cart == null || cart.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "カートが空です");
            return "redirect:/cart";
        }

        //削除処理 removeIf=条件に合う要素を削除
        boolean removed = cart.removeIf(cartItem -> cartItem.getProductId().equals(productId));

        if(!removed){
            redirectAttributes.addFlashAttribute("error","商品が存在しません");
            return "redirect:/cart";
        }

        session.setAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("success", "商品を削除しました");
        return "redirect:/cart";
    }
}
