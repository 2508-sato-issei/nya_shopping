package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.form.MyPageOrderNarrowForm;
import com.example.nya_shopping.dto.OrderHistoryItemDto;
import com.example.nya_shopping.repository.OrderDetailRepository; // ★ 新たに追加
import com.example.nya_shopping.repository.OrderRepository;
import com.example.nya_shopping.repository.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class MyPageService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    /**
     *【日付】
     *1.年が指定されている場合：年または年月で期間を設定
     *2.年と月が未指定の場合：過去3ヶ月をデフォルト期間とする
     *3.月のみが指定されている場合：フロントエンドでエラーとする（ここでは期間指定しない）
     */

    //注文履歴を取得するメソッドを定義（引数にはIDと条件）
    public List<OrderHistoryItemDto> findOrderHistory(Integer userId, MyPageOrderNarrowForm narrowForm) {
        //MuBatis専用FORMを使ってマッピング
        MyPageOrderNarrowForm form= new MyPageOrderNarrowForm();

        //IDとステータスをFORMにセット
        form.setUserId(userId);
        form.setStatus(narrowForm.getStatus());

        //日付の計算をFORMにセット
        Integer year = narrowForm.getYear();
        Integer month = narrowForm.getMonth();

        // 年が指定されている場合
        if (year != null) {
            LocalDate start;
            LocalDate end;

            //1-12月で月も設定されていた場合、その月の1日からその月の末まで。
            if (month != null && month >= 1 && month <= 12) {
                start = LocalDate.of(year, month, 1);
                end = start.withDayOfMonth(start.lengthOfMonth());
            } else { //月が設定されていなかったら、その年全てを表示する。
                start = LocalDate.of(year, 1, 1);
                end = LocalDate.of(year, 12, 31);
            }
            form.setStartDate(start);
            form.setEndDate(end);

            //年も月も入力されていなかった場合
        } else if (year == null && month == null) {


            // 過去3ヶ月をデフォルト設定
            LocalDate today = LocalDate.now();
            LocalDate threeMonthsAgo = today.minusMonths(3);

            // 3ヶ月前の月の1日から今日までを期間とする
            form.setStartDate(threeMonthsAgo.withDayOfMonth(1));
            form.setEndDate(today);
        }

        List<Order> orders = orderRepository.findOrder(form);
        // 3. DTOへのマッピングと totalItems の計算 (★性能の懸念点あり)
        return orders.stream()
                .map(order -> {
                    // OrderDetailRepositoryを使って購入点数を計算 (N+1問題の箇所)
                    Integer totalItems = orderDetailRepository.sumQuantityByOrderId(order.getId());

                    // DTOを生成し、値をマッピング
                    OrderHistoryItemDto dto = new OrderHistoryItemDto();
                    dto.setId(order.getId());
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setStatus(order.getStatus());
                    dto.setCreatedAt(order.getCreatedAt());

                    // 計算した購入点数をセット
                    dto.setTotalItems(totalItems != null ? totalItems : 0);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 注文実績のある年を取得する (HTMLの選択肢用)
     */
    public List<Integer> findOrderYearsByUserId(Integer userId) {
        // 実際はDBアクセスが必要です
        // 例: return orderRepository.findOrderYearsByUserId(userId);
        return List.of(2025, 2024, 2023); // 仮のデータを返す

    }
}
