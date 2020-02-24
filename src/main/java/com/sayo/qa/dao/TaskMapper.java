package com.sayo.qa.dao;

import com.sayo.qa.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface TaskMapper {
    int deleteByPrimaryKey(Integer taskId);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Integer taskId);
    List<Task> selectByQaId(int qaId);
    List<Task> selectTaskByQaTypeAndEid(String type,int EId);
    List<Task> findFinishedTaskByInspector(int inspectorId);
    List<Task> findhasAssignTaskByEid(int eId);
    List<Task> findwaitingAssignTaskByEid(int eId);
    List<Task> findTaskforCheckIns();
    List<Task> findfinishAssignTaskByEidAndStatus(int eId,int status);
    Task selectTaskByTaskId(int taskId);

    int updateByPrimaryKeySelective(Task record);

//    int updateEndtimeById(Date date,int taskId);

    int updateByPrimaryKey(Task record);

    List<Task> selectTaskByInspectorId0(int inspectorId);
}