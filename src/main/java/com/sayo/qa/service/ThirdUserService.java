package com.sayo.qa.service;

import com.sayo.qa.dao.TaskMapper;
import com.sayo.qa.dao.ThirdUserMapper;
import com.sayo.qa.entity.Task;
import com.sayo.qa.entity.ThirdUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThirdUserService {
    @Autowired
    ThirdUserMapper thirdUserMapper;

    @Autowired
    private TaskMapper taskMapper;

    public Map<String,Object> loginThirdUser(String name,String password){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(name)){
            map.put("nameMsg","姓名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        //判断该User存在与否
        ThirdUser user= thirdUserMapper.selectByThirdUsername(name);
        if (user == null){
            map.put("nameMsg","该用户不存在");
            return map;
        }
        if (!password.equals(user.getPassword())){
            map.put("passwordMsg","密码不正确");
            return map;
        }
        return map;
    }

    public List<Task> getTasks(int qaId){
        return taskMapper.selectByQaId(qaId);
    }

}
