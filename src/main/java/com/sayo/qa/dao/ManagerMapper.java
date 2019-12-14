package com.sayo.qa.dao;

import com.sayo.qa.entity.Manager;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ManagerMapper {
    int deleteByManagerId(Integer id);

    int insertManager(Manager manager);

    Manager selectByManagerId(Integer id);

    int updateByManagerId(Manager record);
}