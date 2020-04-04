package com.sayo.qa.service;

import com.sayo.qa.dao.EnterpriseMapper;
import com.sayo.qa.entity.Enterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}