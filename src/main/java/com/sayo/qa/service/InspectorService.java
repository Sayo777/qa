package com.sayo.qa.service;

import com.sayo.qa.dao.InspectorMapper;
import com.sayo.qa.entity.Inspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InspectorService {
    @Autowired
    InspectorMapper inspectorMapper;

    public List<Inspector> findInspectors(){
        return inspectorMapper.selectInspector();
    }

    public Inspector findInspectorById(int id){
        return inspectorMapper.selectByPrimaryKey(id);
    }

    public List<Inspector> findInspectorsByType(String type){
        return inspectorMapper.selectInspectorByType(type);
    }


}
