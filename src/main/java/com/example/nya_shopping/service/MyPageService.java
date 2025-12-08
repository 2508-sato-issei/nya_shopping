package com.example.nya_shopping.service;

import com.example.nya_shopping.controller.form.MyPageOrderNarrowForm;
import com.example.nya_shopping.dto.OrderHistoryItemDto;
import com.example.nya_shopping.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class MyPageService {
    @Autowired
    OrderRepository orderRepository;

    /**
     *【日付】
     *1.年が指定されている場合：年または年月で期間を設定
     *2.年と月が未指定の場合：全部表示
     *3.月のみが指定されている場合：エラーとする(コントローラーで対応済み)
     *
     */

    //注文履歴を取得するメソッドを定義（引数にはIDと条件）
    public List<OrderHistoryItemDto> findOrderHistory(Integer userId, MyPageOrderNarrowForm narrowForm, int offset, int limit) {

        //フォームの準備と日付計算の実行
        setFormDefaultValues(userId, narrowForm);

        //注文履歴をLIMIT/OFFSET付きで取得
        return orderRepository.findOrderHistory(userId, narrowForm, offset, limit);
    }

    public int countOrderHistory(Integer userId, MyPageOrderNarrowForm narrowForm) {
        //フォームの準備と日付計算の実行 (findOrderHistoryと同じロジック)
        setFormDefaultValues(userId, narrowForm);

        //LIMIT/OFFSETなしで、総件数のみをリポジトリに問い合わせる
        return orderRepository.countOrderHistory(userId, narrowForm);
    }

    /**
     * 注文実績のある年を取得する (HTMLの選択肢用)
     */
    public List<Integer> findOrderYearsByUserId(Integer userId) {
        return orderRepository.findOrderYearsByUserId(userId);
    }

    private void setFormDefaultValues(Integer userId, MyPageOrderNarrowForm narrowForm) {

        //IDをFORMにセット
        narrowForm.setUserId(userId);

        //日付の計算をFORMにセット
        Integer year = narrowForm.getYear();
        Integer month = narrowForm.getMonth();

        //年が指定されている場合
        if (year != null) {
            LocalDate start;
            LocalDate end;

            if (month != null && month >= 1 && month <= 12) {
                start = LocalDate.of(year, month, 1);
                end = start.withDayOfMonth(start.lengthOfMonth());
            } else { //月が設定されていなかったら、その年全てを表示する。
                start = LocalDate.of(year, 1, 1);
                end = LocalDate.of(year, 12, 31);
            }
            narrowForm.setStartDate(start);
            narrowForm.setEndDate(end);

            //年も月も入力されていなかった場合 (全部表示 - NULLのままにする)
        } else if (year == null && month == null) {
            narrowForm.setStartDate(null);
            narrowForm.setEndDate(null);
        }
        //月のみが指定されている場合は、コントローラーでエラー処理済み。
    }
}
