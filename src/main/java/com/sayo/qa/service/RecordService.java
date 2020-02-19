package com.sayo.qa.service;

import com.sayo.qa.dao.RecordMapper;
import com.sayo.qa.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    @Autowired
    private RecordMapper recordMapper;

    public int addRecord(Record record){
        int i = recordMapper.insertSelective(record);
        return i;
    }

    public Record findRecordByTaskId(int taskId){
        return recordMapper.selectByPrimaryKey(taskId);
    }
}
