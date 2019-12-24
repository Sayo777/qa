package com.sayo.qa.dao;

import com.sayo.qa.entity.TaskCheck;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskCheckMapper {
    int deleteByPrimaryKey(Integer reqId);

    int insert(TaskCheck record);

    int insertSelective(TaskCheck record);

    TaskCheck selectByPrimaryKey(Integer reqId);
//    List<TaskCheck> selectTaskCheckByResult(String result);

    int updateByPrimaryKeySelective(TaskCheck record);

    int updateByPrimaryKey(TaskCheck record);
}