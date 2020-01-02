package com.sayo.qa.dao;

import com.sayo.qa.entity.Thirdqa;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ThirdqaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Thirdqa record);

    int insertSelective(Thirdqa record);

    Thirdqa selectByPrimaryKey(Integer id);
    List<Thirdqa> selectThirdqas();
    int selectIdByName(String name);

    int updateByPrimaryKeySelective(Thirdqa record);

    int updateByPrimaryKey(Thirdqa record);
}