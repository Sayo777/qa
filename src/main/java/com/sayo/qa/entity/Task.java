package com.sayo.qa.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Task {
    private Integer taskId;

    private String qaType;

    private Integer qaEid;

    private Integer inspectorOne;

    private Integer inspectorTwo;

    private Date startTime;

    private Date endTime;


}