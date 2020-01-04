package com.sayo.qa.dao;

import com.sayo.qa.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {
    int deleteByPrimaryKey(Integer taskId);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Integer taskId);
    List<Task> selectByQaId(int qaId);
    List<Task> selectTaskByQaTypeAndEid(String type,int EId);
    Task selectTaskByTaskId(int taskId);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);

    List<Task> selectTaskByInspectorId0(int inspectorId);
}