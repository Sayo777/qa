package com.sayo.qa.entity;

import lombok.Data;

import java.util.Date;

@Data
public class VoTask {
    private int taskId;
    private Enterprise qaEnterprise;
    private Customer qaCustomer;
    private String contact;
    private String clothType;
    private Date startTime;
    private String address;
}
