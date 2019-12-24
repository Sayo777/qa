package com.sayo.qa.dao;

import com.sayo.qa.entity.ReqArrange;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReqArrangeMapper {
    int deleteByPrimaryKey(Integer requestId);

    int insert(ReqArrange record);

    int insertSelective(ReqArrange record);

    ReqArrange selectByPrimaryKey(Integer requestId);
    List<ReqArrange> selectReqByIsArrange(int isArrange);

    int updateByPrimaryKeySelective(ReqArrange record);

    int updateByPrimaryKey(ReqArrange record);
}