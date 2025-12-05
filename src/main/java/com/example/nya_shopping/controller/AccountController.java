package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.UserForm;
import com.example.nya_shopping.controller.form.UserRegisterForm;
import com.example.nya_shopping.service.AccountService;
import com.example.nya_shopping.validation.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountController {

    @Autowired
    AccountService accountService;

    //アカウント登録画面表示
    @GetMapping("/user/register")
    public String showRegister(Model model){
        model.addAttribute("userRegisterForm", new UserRegisterForm());
        return "user/register";
    }

    //アカウント登録機能
    @PostMapping("/user/register")
    public String registerUser(@ModelAttribute @Validated UserRegisterForm userRegisterForm,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes){

        //パスワードと確認用が一致しているのか確認一致しない場合（E0017）
        if(userRegisterForm.getPassword() != null && userRegisterForm.getConfirmPassword() != null &&
                !userRegisterForm.getPassword().equals(userRegisterForm.getConfirmPassword())){
            result.rejectValue("password", "E0017", ErrorMessage.E0017);
        }

        //メールアドレス重複チェック
        if(userRegisterForm.getEmail() != null && !userRegisterForm.getEmail().isEmpty() &&
                accountService.isEmailDuplicate(userRegisterForm.getEmail())){
            result.rejectValue("email", "E0016", ErrorMessage.E0016);

        }

        if (result.hasErrors()) {
            return "user/register";
        }

        // 問題なければ登録
        accountService.registerNewUser(userRegisterForm);

        return "redirect:/login";
    }


}


