package com.sayo.qa.dao;

import com.sayo.qa.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTicketMapper {
    int insert(LoginTicket record);
    LoginTicket selectByTicket(String ticket);
    int updateByTicket(String ticket);
}