package com.sayo.qa.service;

import com.sayo.qa.dao.ThirdqaMapper;
import com.sayo.qa.entity.Customer;
import com.sayo.qa.entity.EApply;
import com.sayo.qa.entity.Thirdqa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThirdqaService {
    @Autowired
    private ThirdqaMapper thirdqaMapper;

    public List<Thirdqa> findThirdqas(){
        return thirdqaMapper.selectThirdqas();
    }

    public int findIdByName(String name){
        return thirdqaMapper.selectIdByName(name);
    }

    public Thirdqa findThirdqaById(int id){
        return thirdqaMapper.selectByPrimaryKey(id);
    }

    //企业信息认证申请
    public Map<String, Object> apply(Thirdqa thirdqa) {
        //申请企业认证
        Map<String, Object> map = new HashMap<>();
        Thirdqa t = thirdqaMapper.selectByName(thirdqa.getThirdName());
        if (t != null && t.getNotes().equals("通过")) { //已经认证通过了
            map.put("hasApplied", "该企业已经认证过了");
            return map;
        }
        if (t == null) { //企业信息表添加企业信息
            thirdqaMapper.insertSelective(thirdqa);
        }
        return map;
    }

    public List<Thirdqa> findThirdqasByNotes(String notes,int offset,int limit){
        return thirdqaMapper.selectThirdqasByNotes(notes,offset,limit);
    }

    public int findRowsThirdqasByNotes(String notes){
        return thirdqaMapper.selectRowsThirdqasByNotes(notes);
    }

    public int updateNotesById(int id, String status) {
        return thirdqaMapper.updateNotesById(id,status);
    }
}
