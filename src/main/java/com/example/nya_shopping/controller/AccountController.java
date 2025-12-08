package com.example.nya_shopping.controller;

import com.example.nya_shopping.controller.form.UserEditForm;
import com.example.nya_shopping.controller.form.UserForm;
import com.example.nya_shopping.controller.form.UserRegisterForm;
import com.example.nya_shopping.controller.security.LoginUserDetails;
import com.example.nya_shopping.repository.entity.User;
import com.example.nya_shopping.service.AccountService;
import com.example.nya_shopping.validation.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    //会員情報変更画面表示
    @GetMapping("/user/mypage/edit/{id}")
    public String showEditUserForm(@PathVariable String id,
                                   @AuthenticationPrincipal LoginUserDetails loginUser,
                                   Model model,
                                   RedirectAttributes redirectAttributes){


        //IDチェック(空・数字以外)
        if (id == null || id.isEmpty() || !id.matches("^\\d+$")) {
            // IDがnull、空文字列、または数字以外の場合
            redirectAttributes.addFlashAttribute("globalError", ErrorMessage.E0018);
            return "redirect:/user/mypage";
        }

        //Integerに変換
        Integer targetId = Integer.parseInt(id);
        //今ログインしているユーザーのID
        Integer currentUserId = loginUser.getUser().getId();


        //自分以外のIDチェック
        if (!targetId.equals(currentUserId)) {
            redirectAttributes.addFlashAttribute("globalError", ErrorMessage.E0018);
            return "redirect:/user/mypage";
        }

        //ログインしているユーザー情報取得
        User userToEdit = loginUser.getUser();

        //画面遷移
        UserEditForm form = mapUserToEditForm(userToEdit);
        model.addAttribute("userEditForm", form);
        return "user/mypage_edit";
    }

    //ログインユーザーから取得した情報を表示用のFORMに移し替え
    private UserEditForm mapUserToEditForm(User user) {

        UserEditForm form = new UserEditForm();
        form.setUserId(user.getId());
        form.setEmail(user.getEmail());
        form.setName(user.getName());
        form.setPostalCode(user.getPostalCode());

        form.setAddress(user.getAddress());

        form.setPhone(user.getPhone());
        return form;
    }

    //ユーザー編集処理
    @PostMapping("/user/mypage/edit")
    public String editUser(@ModelAttribute @Validated UserEditForm userEditForm,
                           BindingResult result,
                           @AuthenticationPrincipal LoginUserDetails loginUser,
                           RedirectAttributes redirectAttributes) {


        boolean isConfirmPasswordEntered = (userEditForm.getConfirmPassword() != null && !userEditForm.getConfirmPassword().isEmpty());
        boolean isPasswordEntered = (userEditForm.getPassword() != null && !userEditForm.getPassword().isEmpty());

        if (isConfirmPasswordEntered && !isPasswordEntered) {
            // パスワードが入力されていない場合、メインのパスワードフィールドを必須エラーとする
            result.rejectValue("password", "E0005", null, ErrorMessage.E0005);
        }

        if (isPasswordEntered) {
            if (!userEditForm.getPassword().equals(userEditForm.getConfirmPassword())) {
                // エラーを confirmPassword フィールドに紐付け
                result.rejectValue("confirmPassword", "E0017", null, ErrorMessage.E0017);
            }
        }


        //メールアドレスがほかのユーザーと重複していないか確認
        if (userEditForm.getEmail() != null && !userEditForm.getEmail().isEmpty()) {
            if (accountService.isEmailDuplicateExcludingUser(userEditForm.getEmail(), userEditForm.getUserId())) {
                result.rejectValue("email", "E0016", null, ErrorMessage.E0016);
            }
        }

        if (result.hasErrors()) {
            // エラーがある場合はフォームの入力値を保持したまま編集画面に戻る
            return "user/mypage_edit";
        }

        // 問題なければ登録
        User updatedUser = accountService.updateUser(userEditForm);

        // ログイン情報を更新 (セッションのUser Entityを最新情報に置き換える)
        loginUser.setUser(updatedUser);

        // 成功メッセージを付与してリダイレクト
        redirectAttributes.addFlashAttribute("globalMessage", "会員情報を更新しました。");
        return "redirect:/user/mypage";
    }
}


