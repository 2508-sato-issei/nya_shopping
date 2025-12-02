package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.ProductSearchCondition;
import com.example.nya_shopping.dto.ProductDto;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.service.ProductBulkService;
import com.example.nya_shopping.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

    private final ProductService productService;
    private final ProductBulkService productBulkService;

    @GetMapping("/products")
    public String showAdminProducts(@ModelAttribute("cond") ProductSearchCondition cond,
                                    Model model) {

        // ページングに必要な総件数
        int total = productService.count(cond);

        List<ProductDto> products = productService.search(cond);

        model.addAttribute("condition", cond);
        model.addAttribute("products", products);
        model.addAttribute("total", total);
        model.addAttribute("page", cond.getPage());
        model.addAttribute("size", cond.getSize());
        model.addAttribute("categories", Category.values());

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

}
