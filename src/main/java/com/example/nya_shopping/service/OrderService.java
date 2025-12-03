package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.form.OrderNarrowForm;
import com.example.nya_shopping.controller.form.PurchaseForm;
import com.example.nya_shopping.dto.CartItem;
import com.example.nya_shopping.repository.OrderRepository;
import com.example.nya_shopping.repository.entity.Order;
import com.example.nya_shopping.repository.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductService productService;

    public Page<Order> findOrder(OrderNarrowForm form, PageRequest pageRequest) {
        int offset = (int) pageRequest.getOffset();
        int limit = pageRequest.getPageSize();
        //開始時刻をtimestampに変換
        if (form.getStartDate() != null) {
            Timestamp startTs = Timestamp.valueOf(form.getStartDate().atStartOfDay());
            form.setStartTimeStamp(startTs);
        }
        //終了時刻をtimestampに変換
        if (form.getEndDate() != null) {
            Timestamp endTs = Timestamp.valueOf(form.getEndDate().atTime(23, 59, 59));
            form.setEndTimeStamp(endTs);
        }
        List<Order> orderList = orderRepository.findOrder(form, offset, limit);
        int total = orderRepository.countOrder(form, offset, limit);

        return new PageImpl<>(orderList, pageRequest, total);
    }

    //注文を登録する処理
    public int createOrder(PurchaseForm form, List<CartItem> cart) {

        int totalAmount = 0;
        for(CartItem ci : cart){
            Product product = productService.findById(ci.getProductId());
            totalAmount += product.getPrice() * ci.getQuantity();
        }

        Order order = new Order();
        order.setCustomerName(form.getCustomerName());
        order.setCustomerPostalCode(form.getCustomerPostalCode());
        order.setCustomerAddress(form.getCustomerAddress());
        order.setCustomerPhone(form.getCustomerPhone());
        order.setTotalAmount(totalAmount);
        order.setStatus("CONFIRMED");

        orderRepository.insert(order);
        return order.getId();
    }
}