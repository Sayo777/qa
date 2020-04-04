package com.sayo.qa.dao;

import com.sayo.qa.entity.Result;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ResultMapper {
    int insert(Result record);

    int insertSelective(Result record);

    List<Result> selectResults(int offset,int limit);
    int selectResultsRows();

    Result selectResultByTaskId(int taskId);

}