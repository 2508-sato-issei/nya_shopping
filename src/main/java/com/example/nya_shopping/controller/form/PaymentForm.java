package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PaymentForm {
    private Integer id;
    private Integer orderId;
    private String stripeSessionId;
    private String stripePaymentIntent;
    private Integer amount;
    private String currency;
    private String status;
    private Timestamp paidAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
