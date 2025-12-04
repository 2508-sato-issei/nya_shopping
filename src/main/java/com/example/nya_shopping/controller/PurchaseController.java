package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.PurchaseForm;
import com.example.nya_shopping.controller.security.LoginUserDetails;
import com.example.nya_shopping.dto.CartItem;
import com.example.nya_shopping.dto.CartView;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.repository.entity.Product;
import com.example.nya_shopping.service.OrderDetailService;
import com.example.nya_shopping.service.OrderService;
import com.example.nya_shopping.service.PaymentService;
import com.example.nya_shopping.service.ProductService;
import com.stripe.model.checkout.Session;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static com.example.nya_shopping.validation.ErrorMessage.E0019;
import static com.example.nya_shopping.validation.ErrorMessage.E0034;

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
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    //購入者情報入力画面を表示する
    @GetMapping("/order/purchase")
    public String inputPurchase(HttpSession session, Model model, RedirectAttributes redirectAttributes){

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
                redirectAttributes.addFlashAttribute("errorMessage", E0019);
                return "redirect:/cart";
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

    //Stripeのページへ商品情報を送る処理（Checkoutセッションを作成）＝決済画面へ遷移
    @GetMapping("/order/purchase/payment")
    public String createStripe(HttpSession session, Model model){

        //セッションからカートを取得
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        PurchaseForm purchaseForm = (PurchaseForm) session.getAttribute("purchaseForm");
        if(cart == null || cart.isEmpty() || purchaseForm == null){
            model.addAttribute("error", E0034);
            return "redirect:/cart";
        }

        //Stripeセッション作成
        Session sessionObj = paymentService.createCheckoutSession(cart);
        return "redirect:" + sessionObj.getUrl();
    }

    //決済が成功した際の処理
    @GetMapping("/order/purchase/success")
    public String orderSuccess(@RequestParam("session_id") String sessionId,
                               @AuthenticationPrincipal LoginUserDetails loginUser,
                               HttpSession session, Model model){

        //セッションからカートを取得
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        PurchaseForm purchaseForm = (PurchaseForm) session.getAttribute("purchaseForm");
        if(cart == null || cart.isEmpty() || purchaseForm == null){
            model.addAttribute("error", E0034);
            return "redirect:/cart";
        }

        //ログインユーザーID取得
        Integer userId = loginUser.getUser().getId();
        //注文テーブルと注文詳細テーブルに情報を登録
        int orderId = orderService.createOrder(purchaseForm, cart, userId);
        orderDetailService.createOrderDetail(orderId, cart);

        //WebSocketの通知(/topic/paymentに対して通知を送るという指令を出す）
        simpMessagingTemplate.convertAndSend("/topic/payment", "新しい注文が入りました（注文ID: " + orderId + "）");

        //在庫を減らす
        productService.decreaseStock(cart);
        //カートの情報を削除する
        session.removeAttribute("cart");
        session.removeAttribute("purchaseForm");
        model.addAttribute("categories", Category.values());
        return "/user/order-complete";
    }
}
