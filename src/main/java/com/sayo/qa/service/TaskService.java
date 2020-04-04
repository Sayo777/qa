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

    public List<Task> findFinishedTaskByEid(int eId,int offset,int limit){
        return taskMapper.findFinishedTaskByEid(eId,offset,limit);
    }
    public int findFinishedTaskByEidRows(int eId){
        return taskMapper.findFinishedTaskByEidRows(eId);
    }
    public List<Task> findFinishedTaskByQaEid(int qaeId){
        return taskMapper.findFinishedTaskByQaEid(qaeId);
    }

    public List<Task> findTaskByQaTypeAndEid(String type,int EId){
        return taskMapper.selectTaskByQaTypeAndEid(type, EId);
    }

    public List<Task> findFinishedTaskByInspector(int inspectorId){
        return taskMapper.findFinishedTaskByInspector(inspectorId);
    }
    public List<Task> findFinishTaskAll(int offset,int limit){
        return taskMapper.findFinishTaskAll(offset,limit);
    }

    public int findFinishTaskAllRows() {
        return taskMapper.findFinishTaskAllRows();
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
    public List<ReqArrange> findReqByIsArrange(int isArrange,int offset,int limit){
        return reqArrangeMapper.selectReqByIsArrange(isArrange,offset,limit);
    }
    public int findReqByIsArrangeRows(int isArrange){
        return reqArrangeMapper.selectReqByIsArrangeRows(isArrange);
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
    public List<Task> findTaskByInsoector0(int inspectorId,int offset,int limit){
        return taskMapper.selectTaskByInspectorId0(inspectorId,offset,limit);
    }

    public int findTaskByInsoector0Rows(int inspectorId) {
        return taskMapper.selectTaskByInspectorId0Rows(inspectorId);
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
