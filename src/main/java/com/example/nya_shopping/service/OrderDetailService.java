package com.example.nya_shopping.service;

import com.example.nya_shopping.dto.CartItem;
import com.example.nya_shopping.repository.OrderDetailRepository;
import com.example.nya_shopping.repository.entity.OrderDetail;
import com.example.nya_shopping.repository.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    ProductService productService;

    //注文に含まれる全ての商品を登録する
    public void createOrderDetail(int orderId, List<CartItem> cart) {
        for(CartItem ci : cart){
            Product product = productService.findById(ci.getProductId());

            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orderId);
            detail.setProductId(product.getId());
            detail.setProductName(product.getName());
            detail.setProductPrice(product.getPrice());
            detail.setQuantity(ci.getQuantity());
            detail.setSubtotal(product.getPrice() * ci.getQuantity());
            orderDetailRepository.insert(detail);
        }
    }
}
