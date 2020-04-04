package com.sayo.qa.service;

import com.sayo.qa.dao.RequestMapper;
import com.sayo.qa.entity.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    @Autowired
    private RequestMapper requestMapper;


    public int findReqRowByEid(int eId){
        return requestMapper.findReqRowByEid(eId);
    }

    public Request getRequest(int id){
        return requestMapper.selectByPrimaryKey(id);
    }
    public List<Request> findReqByStatus(String status,int offset,int limit){
        return requestMapper.selectReqByStatus(status,offset,limit);
    }
    public List<Request> findRefuseTask(int offset,int limit){
        return requestMapper.findRefuseTask(offset,limit);
    }
    public int findReqByStatusRows(String status){
        return requestMapper.findReqByStatusRows(status);
    }
    public int updateReqStatus(int id,String status){
        return requestMapper.updateReqStatusById(id,status);
    }

    public int findRefuseTaskRows(){
        return requestMapper.findRefuseTaskRows();
    }
}