package com.sayo.qa.dao;

import com.sayo.qa.entity.Sample;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleMapper {
    int deleteByPrimaryKey(Integer taskId);

    int insert(Sample record);

    int insertSelective(Sample record);

    Sample selectByPrimaryKey(Integer taskId);

    int updateByPrimaryKeySelective(Sample record);

    int updateByPrimaryKey(Sample record);

    int updateStatusByid(int id);


}