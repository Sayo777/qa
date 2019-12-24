package com.sayo.qa.service;

import com.sayo.qa.dao.ClothesTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClothesTypeService {
    @Autowired
    private ClothesTypeMapper clothesTypeMapper;

    //获取衣服类型名称
    public String getClothesType(int typeId){
        return clothesTypeMapper.selectByPrimaryKey(typeId).getTypeName();
    }
}
