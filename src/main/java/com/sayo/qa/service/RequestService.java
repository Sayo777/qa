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

    public Request getRequest(int id){
        return requestMapper.selectByPrimaryKey(id);
    }
    public List<Request> findReqByStatus(String status){
        return requestMapper.selectReqByStatus(status);
    }
    public int updateReqStatus(int id,String status){
        return requestMapper.updateReqStatusById(id,status);
    }
}