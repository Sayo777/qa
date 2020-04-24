package com.sayo.qa.dao;

import com.sayo.qa.entity.Enterprise;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EnterpriseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Enterprise record);

    int insertSelective(Enterprise record);

    Enterprise selectByPrimaryKey(Integer id);
    Enterprise selectByEname(String EName);
    List<Enterprise> findEnterpriseByStatus(int status,int offset,int limit);
    int findEnterpriseRowsByStatus(int status);
    int updateByPrimaryKeySelective(Enterprise record);

    int updateByPrimaryKey(Enterprise record);
    int updateStatusById(int id,int status);

    int findEnterpriseStatusByCustomerId(int customerId);
}