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
    //查找某个公司审核通过的申请记录request
    List<Request> selectByReqEId0(Integer reqEId);

    int updateByPrimaryKeySelective(Request record);

    int updateByPrimaryKey(Request record);
}