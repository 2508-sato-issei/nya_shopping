package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.SearchForm;
import com.example.nya_shopping.model.Category;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    //ホーム画面（検索機能付き）
    @GetMapping("/")
    public String showHome(HttpSession session, Model model){

        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        List<Category> popularCategories = List.of(
                Category.PC_AND_PERIPHERALS,
                Category.STATIONERY_AND_SUPPLIES,
                Category.OA_EQUIPMENT
        );
        List<String> popularKeywords = List.of("ノートPC", "プリンタ", "オフィスチェア", "業務用");

        model.addAttribute("popularCategories", popularCategories);
        model.addAttribute("popularKeywords", popularKeywords);
        model.addAttribute("categories", Category.values());
        model.addAttribute("searchForm", new SearchForm());
        return "index";
    }
}
