package com.sayo.qa.service;

import com.sayo.qa.dao.ProductMapper;
import com.sayo.qa.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
