package com.sayo.qa.service;

import com.sayo.qa.dao.ReqArrangeMapper;
import com.sayo.qa.entity.ReqArrange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReqArrangeService {
    @Autowired
    private ReqArrangeMapper reqArrangeMapper;

    public int insertArrange(ReqArrange reqArrange){
        return reqArrangeMapper.insert(reqArrange);
    }
}
