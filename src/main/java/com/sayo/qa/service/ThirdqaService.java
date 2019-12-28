package com.sayo.qa.service;

import com.sayo.qa.dao.ThirdqaMapper;
import com.sayo.qa.entity.Thirdqa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThirdqaService {
    @Autowired
    private ThirdqaMapper thirdqaMapper;

    public List<Thirdqa> findThirdqas(){
        return thirdqaMapper.selectThirdqas();
    }
}
