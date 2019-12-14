package com.sayo.qa.service;

import com.sayo.qa.dao.ManagerMapper;
import com.sayo.qa.entity.Manager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ManagerService {
    @Autowired
    ManagerMapper managerMapper;

    public Map<String, Object> register(String name,String password,String email,int enperpriseId,String type) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(name)) {
            map.put("nameMsg", "名字不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(email)) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }

        // 验证账号
        Manager m = managerMapper.selectByManagerName(name);
        if (m != null) {
            map.put("nameMsg", "该账号已存在!");
            return map;
        }

        // 验证邮箱
        m = managerMapper.selectByManagerEmail(email);
        if (m != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }
        //注册管理员
        managerMapper.insertManagerParty(name, password, email, enperpriseId, type);
        return map;
    }


}
