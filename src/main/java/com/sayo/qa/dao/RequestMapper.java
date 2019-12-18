package com.sayo.qa.dao;

import com.sayo.qa.entity.Request;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RequestMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Request record);

    int insertSelective(Request record);

    Request selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Request record);

    int updateByPrimaryKey(Request record);
}