package com.sayo.qa.service;

import com.sayo.qa.dao.InformationMapper;
import com.sayo.qa.entity.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InformationService {
    @Autowired
    private InformationMapper informationMapper;

    public int addInformation(Information information){
        return informationMapper.insertSelective(information);
    }

    public List<Information> findInformationsBytoid(int toid,int offset,int limit){
        return informationMapper.selectInformationsBytoid(toid,offset,limit);
    }
 public int findRowsInformationsBytoid(int toid){
        return informationMapper.selectRowsInformationsBytoid(toid);
    }
public int findRowsInformationsBytoid0(int toid){
        return informationMapper.selectRowsInformationsBytoid0(toid);
    }

    public int updateStatusBytoId(int toid){
        return informationMapper.updateStatusBytoId(toid);
    }

}
