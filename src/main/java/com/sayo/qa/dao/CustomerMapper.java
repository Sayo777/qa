package com.sayo.qa.dao;

import com.sayo.qa.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Integer id);
    Customer selectCustomerByName(String name);
    Customer selectCustomerByEmail(String email);
    int selectEIdByCustomerId(int customerId);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);
}