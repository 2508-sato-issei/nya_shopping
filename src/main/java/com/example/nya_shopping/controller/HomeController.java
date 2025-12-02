package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.SearchForm;
import com.example.nya_shopping.model.Category;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    //ホーム画面（検索機能付き）
    @GetMapping("/")
    public String showHome(HttpSession session, Model model){
        model.addAttribute("categories", Category.values());
        model.addAttribute("searchForm", new SearchForm());
        return "index";
    }
}
