package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.ProductForm;
import com.example.nya_shopping.controller.form.ProductSearchCondition;
import com.example.nya_shopping.converter.ProductConverter;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.repository.entity.Product;
import com.example.nya_shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ProductService productService;
    @Autowired
    ProductConverter productConverter;

    /* 商品管理一覧表示 */
    @GetMapping("/products")
    public String showProducts(@ModelAttribute("condition") ProductSearchCondition condition,
                               Model model) {

        List<Product> products = productService.searchProducts(condition);

        model.addAttribute("products", products);
        model.addAttribute("condition", condition);

        return "admin/products";
    }

    /* 商品登録画面表示 */
    @GetMapping("/product/new")
    public String showNewProductForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", Category.values());
        return "admin/product_new";
    }

    /* 商品登録処理 */
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

    /* 商品編集画面表示 */
    @GetMapping("/product/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {

        Product product = productService.findById(id);
        ProductForm form = productConverter.toForm(product);

        model.addAttribute("productForm", form);
        model.addAttribute("categories", Category.values());

        return "admin/product_edit";
    }

    /* 商品編集処理 */
    @PostMapping("/admin/products/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Validated @ModelAttribute("productForm") ProductForm form,
                                BindingResult bindingResult,
                                Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", Category.values());
            return "admin/product_edit";
        }

        productService.update(id, form);

        return "redirect:/admin/products";
    }


}
