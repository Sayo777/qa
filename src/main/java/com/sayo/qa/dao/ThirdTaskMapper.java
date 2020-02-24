package com.sayo.qa.dao;

import com.sayo.qa.entity.ThirdTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ThirdTaskMapper {
    int insert(ThirdTask record);

    int insertSelective(ThirdTask record);

    int updateThirdTaskStatus(int taskId);

    int updateStatusByTaskIdAndCode(int statusCode,int taskId);

    int updateThirdTaskIns(int taskId,int ins1,int ins2);

    ThirdTask findThirdTaskById(int taskId);
}