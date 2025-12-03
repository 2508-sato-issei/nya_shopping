package com.example.nya_shopping.service;

import com.example.nya_shopping.dto.CartItem;
import com.example.nya_shopping.repository.entity.Product;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PaymentService {

    private final ProductService productService;

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.checkout.success-url}")
    private String successUrl;

    @Value("${stripe.checkout.cancel-url}")
    private String cancelUrl;

    public PaymentService(ProductService productService){
        this.productService = productService;
    }

    //カート情報からStripe Checkoutセッションを作成し、セッションIDを返す
    public Session createCheckoutSession(List<CartItem> cart) {

        //Stripeに秘密鍵を渡し、誰からのリクエストかを示す
        Stripe.apiKey = secretKey;
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for(CartItem ci : cart){
            Product product = productService.findById(ci.getProductId());
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(ci.getQuantity().longValue())
                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder().setCurrency("jpy")
                    .setUnitAmount(product.getPrice().longValue())
                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(product.getName()).build()
                    )
                    .build()
                    )
                    .build();
            lineItems.add(lineItem);
        }

        SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                .addAllLineItem(lineItems)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .build();

        try{
            return Session.create(params);
        } catch (StripeException e){
            throw new RuntimeException("Stripeの決済セッション作成に失敗しました");
        }
    }
}
