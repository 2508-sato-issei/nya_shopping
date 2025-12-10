package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
public class UserNarrowForm {
    private String email;
    private String name;
    private String address;
    private String phone;
    private Boolean isStopped;
    @DateTimeFormat(pattern = "yyyy-MM-dd") //　値保持
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd") //　値保持
    private LocalDate endDate;
    private Timestamp startTimeStamp;
    private Timestamp endTimeStamp;

    //sort
    private String sort;
    private String order;
}
