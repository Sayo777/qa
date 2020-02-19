package com.sayo.qa.entity;

import java.util.Date;

public class Sample {
    private Integer taskId;

    private String type;

    private Integer basicNumber;

    private Integer sampleNumber;

    private Date sampleDate;

    private String sampleMethod;

    private Integer sampleQuantity;

    private String place;

    private String notes;

    private Integer status;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getBasicNumber() {
        return basicNumber;
    }

    public void setBasicNumber(Integer basicNumber) {
        this.basicNumber = basicNumber;
    }

    public Integer getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(Integer sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public Date getSampleDate() {
        return sampleDate;
    }

    public void setSampleDate(Date sampleDate) {
        this.sampleDate = sampleDate;
    }

    public String getSampleMethod() {
        return sampleMethod;
    }

    public void setSampleMethod(String sampleMethod) {
        this.sampleMethod = sampleMethod == null ? null : sampleMethod.trim();
    }

    public Integer getSampleQuantity() {
        return sampleQuantity;
    }

    public void setSampleQuantity(Integer sampleQuantity) {
        this.sampleQuantity = sampleQuantity;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}