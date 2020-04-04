package com.sayo.qa.dao;

import com.sayo.qa.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);
    int findCountByToId(int toId);

    Message findMessageByToandStatus(int toId,int status);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

}