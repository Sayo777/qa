package com.sayo.qa.service;

import com.sayo.qa.dao.EnterpriseMapper;
import com.sayo.qa.entity.Enterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnterpriseService {
    @Autowired
    private EnterpriseMapper enterpriseMapper;

    public Enterprise getEnterPriseById(int id){
        return enterpriseMapper.selectByPrimaryKey(id);
    }
    public Enterprise getEnterPriseByName(String eName){
        return enterpriseMapper.selectByEname(eName);
    }

    public int findEnterpriseStatusByCustomerId(int customerId){
        return enterpriseMapper.findEnterpriseStatusByCustomerId(customerId);
    }

    public int findEnterpriseRowsByStatus(int status){
        return enterpriseMapper.findEnterpriseRowsByStatus(status);
    }

    public List<Enterprise> findEnterpriseByStatus(int status,int offset,int limit){
        return enterpriseMapper.findEnterpriseByStatus(status,offset,limit);
    }

    public int updateStatusById(int id,int status){
        return enterpriseMapper.updateStatusById(id,status);
    }
}