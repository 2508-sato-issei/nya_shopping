package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.SearchForm;
import com.example.nya_shopping.model.Category;
import com.example.nya_shopping.repository.entity.Product;
import com.example.nya_shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.example.nya_shopping.validation.ErrorMessage.E0018;

@Controller
public class SearchController {

    @Autowired
    ProductService productService;

    //検索機能・ソート機能付き
    @GetMapping("/search")
    public String searchProducts(@ModelAttribute SearchForm searchForm, @RequestParam(defaultValue = "0") int page,
                                 Model model){

        if (searchForm.getSort() == null) {
            searchForm.setSort("");
        }
        Page<Product> resultPage = productService.searchProduct(searchForm, PageRequest.of(page, 20));
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
        boolean showLast = totalPages > 0 && endPage < totalPages - 1;

        // 検索結果
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("results", resultPage.getContent());
        model.addAttribute("categories", Category.values());

        // ページネーション
        model.addAttribute("page", resultPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("showFirst", showFirst);
        model.addAttribute("showLast", showLast);
        return "search";
    }

    //商品詳細画面表示
    @GetMapping("/product/{id}")
    public String detailProduct(@PathVariable Integer id,
                                @RequestParam(required = false) String category,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(required = false) Integer maxPrice,
                                @RequestParam(required = false) Integer minPrice,
                                @RequestParam(required = false) Integer stock,
                                @RequestParam(required = false) String sort,
                                @RequestParam(required = false) Integer page,
                                Model model, RedirectAttributes redirectAttributes){

        Product product = productService.findById(id);

        if (product == null) {
            redirectAttributes.addFlashAttribute("errorMessage", E0018);
            return "redirect:/";
        }

        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("stock", stock);
        model.addAttribute("sort", sort);
        model.addAttribute("page", page);
        model.addAttribute("product", product);
        return "detail";
    }
}
