package com.sayo.qa.dao;

import com.sayo.qa.entity.Information;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InformationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Information record);

    int insertSelective(Information record);

    Information selectByPrimaryKey(Integer id);
    List<Information> selectInformationsBytoid(int toid,int offset,int limit);
    int selectRowsInformationsBytoid(int toid);
    int selectRowsInformationsBytoid0(int toid);

    int updateByPrimaryKeySelective(Information record);

    int updateByPrimaryKey(Information record);

    int updateStatusBytoId(int toid);
}