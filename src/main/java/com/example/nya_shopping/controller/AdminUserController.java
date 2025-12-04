package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.UserNarrowForm;
import com.example.nya_shopping.repository.entity.User;
import com.example.nya_shopping.service.UserService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@MapperScan("com.example.nya_shopping.repository")
public class AdminUserController {

    @Autowired
    UserService userService;
    @GetMapping("/admin/users")
    public String showAdminUser(Model model,
                                @ModelAttribute UserNarrowForm form,
                                @RequestParam(defaultValue = "0") int page){
        Page<User> resultPage = userService.findAllUser
                (form, PageRequest.of(page, 10));
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

        model.addAttribute("form", form);
        model.addAttribute("userList", resultPage);

        // ページネーション
        model.addAttribute("page", resultPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("showFirst", showFirst);
        model.addAttribute("showLast", showLast);
        return "admin/users";
    }
}
