package com.example.nya_shopping.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String postalCode;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String role;

    @Column
    private Boolean isStopped;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;
}
