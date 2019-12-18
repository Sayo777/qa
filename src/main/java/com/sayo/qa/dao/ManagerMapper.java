package com.sayo.qa.dao;

import com.sayo.qa.entity.Manager;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ManagerMapper {
    int deleteByManagerId(Integer id);

    int insertManager(Manager manager);
    //注册的时候只有这几个字段
    int insertManagerParty(String name,String password,String email,int enterpriseId,String type);

    Manager selectByManagerId(Integer id);
    Manager selectByManagerName(String name,String type);
    Manager selectByManagerEmail(String email);

    int updateByManagerId(Manager record);
}




















