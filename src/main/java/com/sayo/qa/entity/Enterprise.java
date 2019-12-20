package com.sayo.qa.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Enterprise {
    private Integer id;

    private String enterpriseName;

    private String enterpriseEmail;

    private String website;

    private String legalPerson;

    private String commercialNumber;

    private String creditCode;

    private String type;

    private String address;

    private String province;

    private String city;

    private String scope;

    private String status;

    private String organCode;

    private String deadline;

    private Date auditDate;

    private String licenseSrc;

    private int isValid;


}