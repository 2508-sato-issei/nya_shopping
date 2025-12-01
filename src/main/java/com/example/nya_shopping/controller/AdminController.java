package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.ProductForm;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ProductService productService;

    @GetMapping("/product/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", Category.values());
        return "admin/product_new";
    }

    @PostMapping("/product/new")
    public String createProduct(@Validated @ModelAttribute("productForm") ProductForm form,
                                BindingResult bindingResult,
                                Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", Category.values());
            return "admin/product_new";
        }

        productService.create(form);

        return "redirect:/admin/products";
    }


}
