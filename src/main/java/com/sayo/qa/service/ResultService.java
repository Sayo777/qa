package com.sayo.qa.service;

import com.sayo.qa.dao.ResultMapper;
import com.sayo.qa.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {
    @Autowired
    private ResultMapper resultMapper;

    public List<Result> findResults(){
        return resultMapper.selectResults();
    }





}
