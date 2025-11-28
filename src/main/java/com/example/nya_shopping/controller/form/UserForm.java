package com.example.nya_shopping.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UserForm {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private String postalCode;
    private String address;
    private String phone;
    private String role;
    private Boolean isStopped;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
