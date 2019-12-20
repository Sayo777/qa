package com.sayo.qa.dao;

import com.sayo.qa.entity.EApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EApply record);

    int insertSelective(EApply record);

    EApply selectByPrimaryKey(Integer id);
    List<EApply> selectByEName(String eName);

    int updateByPrimaryKeySelective(EApply record);

    int updateByPrimaryKey(EApply record);
}