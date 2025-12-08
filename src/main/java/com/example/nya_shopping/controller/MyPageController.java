package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.MyPageOrderNarrowForm;
import com.example.nya_shopping.controller.security.LoginUserDetails;
import com.example.nya_shopping.dto.MyPageDto;
import com.example.nya_shopping.dto.OrderHistoryItemDto;
import com.example.nya_shopping.repository.entity.Order;
import com.example.nya_shopping.repository.entity.User;
import com.example.nya_shopping.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MyPageController {

    @Autowired
    MyPageService myPageService;

    @GetMapping("/user/mypage")
    public String showMyPage(Model model,
                             @AuthenticationPrincipal LoginUserDetails loginUser,
                             @ModelAttribute MyPageOrderNarrowForm form,
                             @RequestParam(defaultValue = "0") int page){

        final int LIMIT = 10; // 1ページあたりの表示件数
        int offset = page * LIMIT;

        //ログインユーザー情報取得
        User user = loginUser.getUser();
        Integer userId = user.getId();

        if (form.getMonth() != null && form.getYear() == null) {

            //エラーメッセージをModelに設定
            model.addAttribute("globalError", "月を指定する場合は、年を必ず指定してください。");

            //画面再表示に必要なデータを再取得
            model.addAttribute("myPageForm", form);
            model.addAttribute("user", user);
            model.addAttribute("orderYears", myPageService.findOrderYearsByUserId(userId));

            //注文履歴を取得せずに画面に戻る
            return "user/mypage";
        }

        int totalCount = myPageService.countOrderHistory(userId, form);

        //注文情報取得（メソッドを活用）
        List<OrderHistoryItemDto> orderHistoryList = myPageService.findOrderHistory(userId, form, offset, LIMIT);

        //注文年を取得
        List<Integer> orderYears = myPageService.findOrderYearsByUserId(userId);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalCount / LIMIT));
        model.addAttribute("totalCount", totalCount);

        //会員情報、注文履歴リスト、注文年リストをModelにセット
        model.addAttribute("user", user);
        model.addAttribute("orderHistoryList", orderHistoryList);
        model.addAttribute("orderYears", orderYears);

        //絞り込みフォーム（画面再表示用）をModelにセット
        model.addAttribute("myPageForm", form);

        //注文状態の選択肢をModelにセット
        List<String> orderStatusOptions = List.of("CONFIRMED", "PREPARING", "SHIPPED");
        model.addAttribute("orderStatusesList", orderStatusOptions);

        return "user/mypage";


    }
}
