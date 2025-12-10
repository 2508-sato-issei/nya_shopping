package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderNarrowForm {

    //検索
    private Integer minAmount;
    private Integer maxAmount;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Timestamp startTimeStamp;
    private Timestamp endTimeStamp;
    //絞り込み
    private String sort;
    private String order;

}
