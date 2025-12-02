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
    public Integer minAmount;
    public Integer maxAmount;
    public String customerName;
    public String customerAddress;
    public String customerPhone;
    public LocalDate startDate;
    public LocalDate endDate;
    public String status;
    public Timestamp startTimeStamp;
    public Timestamp endTimeStamp;
    //絞り込み
    public String sort;
    public String order;

}
