package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
public class UserNarrowForm {
    public String email;
    public String name;
    public String address;
    public String phone;
    public Boolean isStopped;
    public LocalDate startDate;
    public LocalDate endDate;
    public Timestamp startTimeStamp;
    public Timestamp endTimeStamp;

    //sort
    public String sort;
    public String order;
}
