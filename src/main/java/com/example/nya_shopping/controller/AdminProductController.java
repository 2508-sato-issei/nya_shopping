package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.ProductForm;
import com.example.nya_shopping.controller.form.ProductSearchCondition;
import com.example.nya_shopping.dto.ProductDto;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.service.ProductBulkService;
import com.example.nya_shopping.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

    private final ProductService productService;
    private final ProductBulkService productBulkService;

    @GetMapping("/products")
    public String showAdminProducts(@ModelAttribute("cond") ProductSearchCondition cond,
                                    @RequestParam(defaultValue = "0") int page,
                                    Model model) {

        Page<ProductDto> resultPage = productService.search(cond, PageRequest.of(page, 10));
        int totalPages = resultPage.getTotalPages();
        int currentPage = resultPage.getNumber();
        int displayRange = 5;
        int startPage = 0;
        int endPage = 0;

        if (totalPages > 0) {
            startPage = Math.max(0, currentPage - displayRange);
            endPage = Math.min(totalPages - 1, currentPage + displayRange);
        }

        boolean showFirst = totalPages > 0 && startPage > 0;
        boolean showLast = totalPages > 0 && endPage < totalPages -1;

        model.addAttribute("condition", cond);
        model.addAttribute("products", resultPage);
        model.addAttribute("categories", Category.values());

        model.addAttribute("page", resultPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("showFirst", showFirst);
        model.addAttribute("showLast", showLast);

        return "admin/products";
    }

    @GetMapping("/products/export/csv")
    public void exportCsv(@ModelAttribute ProductSearchCondition cond,
                          HttpServletResponse response) throws IOException {

        String fileName = "products_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".csv";

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        productService.exportCsv(cond, response.getWriter());
    }

    @GetMapping("/products/import/csv")
    public String showImportCsv() {
        return "admin/products_csv";
    }

    @PostMapping("/products/import/csv")
    public String ImportCsv(@RequestParam("file") MultipartFile file,
                            Model model) {

        List<String> errors = productBulkService.processCsv(file);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "admin/products_csv";
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/product/new")
    public String showNewForm(Model model) {
        model.addAttribute("form", new ProductForm());
        model.addAttribute("categories", Category.values());
        return "admin/product_new";
    }

    @PostMapping("/product/new")
    public String register(@ModelAttribute("form") ProductForm form, Model model) {

        List<String> errors = productService.validateAndCreate(form);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("categories", Category.values());
            return "admin/product_new";
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/product/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {

        List<String> errors = new ArrayList<>();

        ProductForm form = productService.findById(id);

        model.addAttribute("form", form);
        model.addAttribute("categories", Category.values());
        return "admin/product_edit";
    }

    @PostMapping("/product/edit")
    public String update(@ModelAttribute("form") ProductForm form, Model model) {

        List<String> errors = productService.validateAndUpdate(form);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("categories", Category.values());
            return "admin/product_edit";
        }

        return "redirect:/admin/products";
    }

    @PostMapping("/product/delete")
    public String delete(@RequestParam("id") Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}
