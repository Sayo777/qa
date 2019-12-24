package com.sayo.qa.entity;

import lombok.Data;

import java.util.Date;
@Data
public class TaskCheck {
    private Integer reqId;

    private String result;

    private String reason;

    private Integer checkerId;

    private Date checkTime;
}