package com.sayo.qa.dao;

import com.sayo.qa.entity.ClothesType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClothesTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClothesType record);

    int insertSelective(ClothesType record);

    ClothesType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClothesType record);

    int updateByPrimaryKey(ClothesType record);
}