package com.sayo.qa.dao;

import com.sayo.qa.entity.ThirdUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ThirdUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ThirdUser record);

    int insertSelective(ThirdUser record);

    ThirdUser selectByPrimaryKey(Integer id);
    ThirdUser selectByThirdUsername(String name);

    int updateByPrimaryKeySelective(ThirdUser record);

    int updateByPrimaryKey(ThirdUser record);
}