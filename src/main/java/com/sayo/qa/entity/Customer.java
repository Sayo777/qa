package com.sayo.qa.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Customer {
    private Integer id;

    private String name;

    private String password;

    private String email;

    private String gender;

    private String phone;

    private Date birth;

    private Integer enterpriseId;
}