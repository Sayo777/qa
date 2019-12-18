package com.sayo.qa.dao;

import com.sayo.qa.entity.Enterprise;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnterpriseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Enterprise record);

    int insertSelective(Enterprise record);

    Enterprise selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Enterprise record);

    int updateByPrimaryKey(Enterprise record);
}