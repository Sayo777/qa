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

    public int updateReqArrange(int reqId){
        return reqArrangeMapper.updateByReqId(reqId);
    }

    public ReqArrange findReqArrangeByReqId(int reqId){
        return reqArrangeMapper.selectByPrimaryKey(reqId);
    }

    public int updateGaozhishu(int reqId){
        return reqArrangeMapper.updateGaozhishuById(reqId);
    }

}
