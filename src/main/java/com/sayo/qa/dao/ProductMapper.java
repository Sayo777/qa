package com.sayo.qa.dao;

import com.sayo.qa.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    int deleteByPrimaryKey(Integer productId);

    int insert(Product record);
    Product findSameProductByEid(int eId,String productName,String guige);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer productId);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> findProductByEid(int Eid,int offset,int limit);
    List<Product> findProductByEid1(int Eid);

    int findProductRows(int Eid);
}