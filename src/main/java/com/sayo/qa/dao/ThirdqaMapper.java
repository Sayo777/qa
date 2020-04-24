package com.sayo.qa.dao;

import com.sayo.qa.entity.Thirdqa;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ThirdqaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Thirdqa record);

    int insertSelective(Thirdqa record);

    Thirdqa selectByPrimaryKey(Integer id);
    Thirdqa selectByName(String name);
    List<Thirdqa> selectThirdqas();
    List<Thirdqa> selectThirdqasByNotes(String notes,int offset,int limit);
    int selectRowsThirdqasByNotes(String notes);
    int selectIdByName(String name);

    int updateByPrimaryKeySelective(Thirdqa record);

    int updateByPrimaryKey(Thirdqa record);
    int updateNotesById(int id,String status);
}