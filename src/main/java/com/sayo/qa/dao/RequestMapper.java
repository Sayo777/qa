package com.sayo.qa.dao;

import com.sayo.qa.entity.Request;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RequestMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Request record);

    int insertSelective(Request record);

    Request selectByPrimaryKey(Integer id);
    List<Request> selectByReqId(Integer reqId);
    List<Request> selectByReqEId(Integer reqEId);
    //查找某个公司审核通过的申请记录request
    List<Request> selectByReqEId0(Integer reqEId);
    //查找任务请求的处理status来查找任务
    List<Request> selectReqByStatus(String status);

    int updateByPrimaryKeySelective(Request record);
    int updateReqStatusById(int id,String status);

    int updateByPrimaryKey(Request record);
}