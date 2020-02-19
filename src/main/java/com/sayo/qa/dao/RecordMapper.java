package com.sayo.qa.dao;

import com.sayo.qa.entity.Record;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordMapper {
    int deleteByPrimaryKey(Integer taskId);

    int insert(Record record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(Integer taskId);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);
}