package com.sayo.qa.service;

import com.sayo.qa.dao.ReqArrangeMapper;
import com.sayo.qa.dao.TaskCheckMapper;
import com.sayo.qa.dao.TaskMapper;
import com.sayo.qa.entity.ReqArrange;
import com.sayo.qa.entity.Task;
import com.sayo.qa.entity.TaskCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskCheckMapper taskCheckMapper;
    @Autowired
    private ReqArrangeMapper reqArrangeMapper;

    public List<Task> findTaskByQaTypeAndEid(String type,int EId){
        return taskMapper.selectTaskByQaTypeAndEid(type, EId);
    }

    public List<Task> findFinishedTaskByInspector(int inspectorId){
        return taskMapper.findFinishedTaskByInspector(inspectorId);
    }

    public List<Task> findTaskforCheckIns(){
        return taskMapper.findTaskforCheckIns();
    }

    public Task findTaskByTaskId(int taskId){
        return taskMapper.selectTaskByTaskId(taskId);
    }
    public int insertTaskCheck(TaskCheck taskCheck){
        return taskCheckMapper.insert(taskCheck);
    }

    //根据是否被安排查找任务
    public List<ReqArrange> findReqByIsArrange(int isArrange){
        return reqArrangeMapper.selectReqByIsArrange(isArrange);
    }

    public TaskCheck findTaskCheckByReqId(int reqId){
        return taskCheckMapper.selectByPrimaryKey(reqId);
    }

    public int addTaskSelective(Task task){
        return taskMapper.insertSelective(task);
    }

    /**
     * 未被执行的task（只能看见自己的任务，所在公司的任务是不是也可以查看？？？？？？？？？）
     * @param inspectorId 质检员
     * @return
     */
    public List<Task> findTaskByInsoector0(int inspectorId){
        return taskMapper.selectTaskByInspectorId0(inspectorId);
    }

    public List<Task> findhasAssignTaskByEid(int eId){

        return taskMapper.findhasAssignTaskByEid(eId);
    }
    public List<Task> findfinishAssignTaskByEidAndStatus(int eId,int status){

        return taskMapper.findfinishAssignTaskByEidAndStatus(eId,status);
    }
    public List<Task> findwaitingAssignTaskByEid(int eId){

        return taskMapper.findwaitingAssignTaskByEid(eId);
    }

    public int updateTaskSelective(Task task){
        return taskMapper.updateByPrimaryKeySelective(task);
    }



}
