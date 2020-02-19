package com.sayo.qa.service;

import com.sayo.qa.dao.SampleMapper;
import com.sayo.qa.entity.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
    @Autowired
    private SampleMapper sampleMapper;

    public Sample findSampleByTaskId(int taskId){
        return sampleMapper.selectByPrimaryKey(taskId);
    }

    public int addSample(Sample sample){
        return sampleMapper.insertSelective(sample);
    }
}
