package com.sayo.qa.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Request {
    private Integer id;

    private Integer reqId;

    private Integer reqEid;

    private String contact;

    private Integer reqType;

    private Date reqTime;

    private String status;

    private String reason;

    private Integer productId;
}