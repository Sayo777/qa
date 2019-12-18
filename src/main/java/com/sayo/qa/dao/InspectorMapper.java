package com.sayo.qa.dao;

import com.sayo.qa.entity.Inspector;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InspectorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Inspector record);
    int insertPart(String name,String password,String email,int enterpriseId,String type);

    int insertSelective(Inspector record);

    List<Inspector> selectInspector();
    Inspector selectByPrimaryKey(Integer id);
    Inspector selectInspectorByName(String name,String type);
    Inspector selectInspectorByEmail(String email);

    int updateByPrimaryKeySelective(Inspector record);

    int updateByPrimaryKey(Inspector record);

}