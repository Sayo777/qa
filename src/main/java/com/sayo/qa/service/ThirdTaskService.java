package com.sayo.qa.service;

import com.sayo.qa.dao.ThirdTaskMapper;
import com.sayo.qa.entity.ThirdTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThirdTaskService {
    @Autowired
    private ThirdTaskMapper thirdTaskMapper;

    public int addThirdTask(ThirdTask thirdTask){
        return thirdTaskMapper.insertSelective(thirdTask);
    }

    public int updateThirdTaskStatus(int taskId){
        return thirdTaskMapper.updateThirdTaskStatus(taskId);
    }

    public int updateThirdTaskIns(int taskId,int ins1 ,int ins2){
        return thirdTaskMapper.updateThirdTaskIns(taskId,ins1,ins2);
    }
    public ThirdTask findThirdTaskById(int taskId){
        return thirdTaskMapper.findThirdTaskById(taskId);
    }
    public List<ThirdTask> findTaskByStatus(int status){
        return thirdTaskMapper.findTaskByStatus(status);
    }
    public int updateStatusByTaskIdAndCode(int statusCode,int taskId){
        return thirdTaskMapper.updateStatusByTaskIdAndCode(statusCode,taskId);
    }
}
