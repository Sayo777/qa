package com.sayo.qa.service;

import com.sayo.qa.dao.TaskCheckMapper;
import com.sayo.qa.entity.TaskCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskCheckService {
    @Autowired
    private TaskCheckMapper taskCheckMapper;

    public TaskCheck findTaskCheckByReqId(int id){
        return taskCheckMapper.selectByPrimaryKey(id);
    }
}
