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

        int totalPages = (int) Math.ceil((double) totalCount / LIMIT);
        int currentPage = page; // 現在のページ番号
        int displayRange = 5; // 表示するページ番号の範囲（例: 5ページ分）
        int startPage = 0;
        int endPage = 0;

        if (totalPages > 0) {
            // 表示開始ページを計算 (0 または 現在ページ - 範囲)
            startPage = Math.max(0, currentPage - displayRange);
            // 表示終了ページを計算 (最終ページ または 現在ページ + 範囲)
            endPage = Math.min(totalPages - 1, currentPage + displayRange);
        }

        // 「最初へ」「最後へ」ボタンの表示制御フラグ
        boolean showFirst = totalPages > 0 && startPage > 0;
        boolean showLast = totalPages > 0 && endPage < totalPages - 1;

        // pe-ji
        model.addAttribute("page", null);
        model.addAttribute("size", LIMIT); // 1ページあたりの件数
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("showFirst", showFirst);
        model.addAttribute("showLast", showLast);

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
