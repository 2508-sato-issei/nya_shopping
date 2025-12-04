package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

//マイページ専用絞り込み
@Getter
@Setter
public class MyPageOrderNarrowForm {

    //userID
    private Integer userId;

    //注文状態
    private String status;

    //注文年
    private Integer year;

    //注文月
    private Integer month;

    //開始日
    private LocalDate startDate;

    //終了日
    private LocalDate endDate;

}