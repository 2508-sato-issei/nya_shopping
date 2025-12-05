package com.example.nya_shopping.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

import static com.example.nya_shopping.validation.ErrorMessage.*;

@Getter
@Setter
public class UserRegisterForm {
    private Integer id;

    @NotBlank(message = E0004)
    @Email(message = E0010)
    private String email;

    //【必須チェック】[パスワード] E0005
    //【形式、字数チェック】[パスワード] 半角英数字混在、8〜32文字 E0011
    @NotBlank(message = E0005)
    @Pattern(regexp = "^[a-zA-Z0-9]{8,32}$", message = E0011)
    private String password;

    private String confirmPassword;

    @NotBlank(message = E0006)
    @Size(max = 50, message = E0012)
    private String name;

    @NotBlank(message=E0007)
    @Pattern(regexp = "\\d{7}", message = E0013)
    private String postalCode;

    @NotBlank(message=E0008)
    @Size(max = 100, message = E0014)
    private String address;

    @NotBlank(message=E0009)
    @Pattern(regexp = "\\d{10,11}", message = E0015)
    private String phone;
}