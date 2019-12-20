package com.sayo.qa.service;

import com.sayo.qa.dao.RequestMapper;
import com.sayo.qa.entity.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    @Autowired
    private RequestMapper requestMapper;

    public Request getRequest(int id){
        return requestMapper.selectByPrimaryKey(id);
    }
}