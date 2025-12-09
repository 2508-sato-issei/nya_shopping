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

        boolean isPasswordEntered = userRegisterForm.getPassword() != null && !userRegisterForm.getPassword().isEmpty();
        boolean isConfirmEntered = userRegisterForm.getConfirmPassword() != null && !userRegisterForm.getConfirmPassword().isEmpty();

        // ①片方だけ入力されている場合
        if (isPasswordEntered && !isConfirmEntered) {
            result.rejectValue("confirmPassword", "E0017", ErrorMessage.E0017);
        }
        if (!isPasswordEntered && isConfirmEntered) {
            result.rejectValue("confirmPassword", "E0017", ErrorMessage.E0017);
        }

        // ②両方入力されている → 一致チェック
        if (isPasswordEntered && isConfirmEntered) {

            if (!userRegisterForm.getPassword().equals(userRegisterForm.getConfirmPassword())) {
                // 不一致の場合は形式チェックを行わない
                result.rejectValue("confirmPassword", "E0017", ErrorMessage.E0017);

            } else {
                // 一致 → 形式チェック
                if (!userRegisterForm.getPassword().matches("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,32}")) {
                    result.rejectValue("password", "E0011", ErrorMessage.E0011);
                }
            }
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
            redirectAttributes.addFlashAttribute("errorMessage", ErrorMessage.E0018);
            return "redirect:/";
        }

        //Integerに変換
        Integer targetId = Integer.parseInt(id);
        //今ログインしているユーザーのID
        Integer currentUserId = loginUser.getUser().getId();


        //自分以外のIDチェック
        if (!targetId.equals(currentUserId)) {
            redirectAttributes.addFlashAttribute("errorMessage", ErrorMessage.E0018);
            return "redirect:/";
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

    @GetMapping("/user/mypage/edit/")
    public String redirect(@AuthenticationPrincipal LoginUserDetails loginUser,
                           RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage", ErrorMessage.E0018);
        return "redirect:/";
    }

    //ユーザー編集処理
    @PostMapping("/user/mypage/edit")
    public String editUser(@ModelAttribute @Validated UserEditForm userEditForm,
                           BindingResult result,
                           @AuthenticationPrincipal LoginUserDetails loginUser,
                           RedirectAttributes redirectAttributes) {


        boolean isPasswordEntered =
                userEditForm.getPassword() != null && !userEditForm.getPassword().isEmpty();

        boolean isConfirmEntered =
                userEditForm.getConfirmPassword() != null && !userEditForm.getConfirmPassword().isEmpty();


// ①片方だけ入力されている → 不一致エラー（E0017）
        if (isPasswordEntered && !isConfirmEntered) {
            result.rejectValue("confirmPassword", "E0017", ErrorMessage.E0017);
        }

        if (!isPasswordEntered && isConfirmEntered) {
            result.rejectValue("confirmPassword", "E0017", ErrorMessage.E0017);
        }



        if (isPasswordEntered && isConfirmEntered) {

            if (!userEditForm.getPassword().equals(userEditForm.getConfirmPassword())) {
                // 不一致 → 形式チェックはしない
                result.rejectValue("confirmPassword", "E0017", ErrorMessage.E0017);

            } else {
                // 一致 → 形式チェック
                if (!userEditForm.getPassword().matches("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,32}")) {
                    result.rejectValue("password", "E0011", ErrorMessage.E0011);
                }
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


