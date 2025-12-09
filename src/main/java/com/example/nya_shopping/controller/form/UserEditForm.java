package com.example.nya_shopping.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; // ※パスワード欄からは削除
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static com.example.nya_shopping.validation.ErrorMessage.*;

@Getter
@Setter
public class UserEditForm {

    // 必須: 更新対象のID
    private Integer userId; // user.idを格納

    @NotBlank(message = E0004)
    @Pattern(
            regexp = "^$|^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = E0010
    )
    private String email;


    private String password;

    private String confirmPassword;

    @NotBlank(message = E0006)
    @Size(max = 50, message = E0012)
    private String name;

    @NotBlank(message=E0007)
    @Pattern(regexp = "^$|\\d{7}", message = E0013)
    private String postalCode;

    @NotBlank(message=E0008)
    @Size(max = 100, message = E0014)
    private String address;

    @NotBlank(message=E0009)
    @Pattern(regexp = "^$|\\d{10,11}", message = E0015)
    private String phone;
}