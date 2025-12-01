package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.SearchForm;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String showHome(HttpSession session, Model model){

        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        model.addAttribute("categories", Category.values());
        model.addAttribute("searchForm", new SearchForm());
        return "index";
    }
}
