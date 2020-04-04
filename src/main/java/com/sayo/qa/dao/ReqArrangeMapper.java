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
    List<ReqArrange> selectReqByIsArrange(int isArrange,int offset,int limit);
    int selectReqByIsArrangeRows(int isArrange);

    int updateByPrimaryKeySelective(ReqArrange record);
    int updateByReqId(int requestId);
    int updateByPrimaryKey(ReqArrange record);
    int updateGaozhishuById(int reqId);
}