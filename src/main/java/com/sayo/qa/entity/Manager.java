package com.sayo.qa.entity;

import java.util.Date;

public class Manager {
    private Integer id;

    private String name;

    private String password;

    private String gender;

    private String phone;

    private String email;

    private Date birth;

    private Integer enterpriseId;

    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Manager(){
    }

    public Manager(Integer id, String name, String password, String gender, String phone, String email, Date birth, Integer enterpriseId, String type) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.birth = birth;
        this.enterpriseId = enterpriseId;
        this.type = type;
    }
}