package com.example.nya_shopping.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static com.example.nya_shopping.validation.ErrorMessage.*;

@Getter
@Setter
//購入者情報入力画面で使用
public class PurchaseForm {

    @NotBlank(message = E0006)
    @Size(max = 50, message = E0012)
    private String customerName;

    @NotBlank(message = E0007)
    @Pattern(regexp = "|\\d{7}", message = E0013)
    private String customerPostalCode;

    @NotBlank(message = E0008)
    @Size(max = 100, message = E0014)
    private String customerAddress;

    @NotBlank(message = E0009)
    @Pattern(regexp = "|\\d{10,11}", message = E0015)
    private String customerPhone;
}
