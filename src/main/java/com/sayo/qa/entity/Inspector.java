package com.sayo.qa.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Inspector {
    private Integer id;

    private String name;

    private String password;

    private String gender;

    private String phone;

    private String email;

    private String eduDegree;

    private Date birth;

    private String type;

    private String address;

    private String province;

    private String city;

    private Integer enterpriseId;

    private String proofSrc;

    private String identityCard;

    private int inspectType;



}