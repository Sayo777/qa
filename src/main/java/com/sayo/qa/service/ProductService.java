package com.sayo.qa.service;

import com.sayo.qa.dao.ProductMapper;
import com.sayo.qa.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public int addProduct(Product product){
        return productMapper.insertSelective(product);
    }

    public Product findSameProduct(int eId,String pname,String guige){
        return productMapper.findSameProductByEid(eId, pname, guige);
    }

    public Product findProductById(int id){
        return productMapper.selectByPrimaryKey(id);
    }

    public List<Product> findProductByEid(int Eid,int offset,int limit){
        return productMapper.findProductByEid(Eid,offset,limit);
    }
    public List<Product> findProductByEid1(int Eid){
        return productMapper.findProductByEid1(Eid);
    }


    public int delProductById(int id){
        return productMapper.deleteByPrimaryKey(id);
    }

    public int findProductRows(int eId){
        return productMapper.findProductRows(eId);
    }
}
