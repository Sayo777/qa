package com.sayo.qa.service;

import com.sayo.qa.dao.AdminMapper;
import com.sayo.qa.entity.Admin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public Map<String,Object> login(String name,String password){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(name)) {
            map.put("nameMsg", "姓名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        //判断该Admin存在与否
        Admin admin = adminMapper.selectAdminByName(name);
        if (admin == null) {
            map.put("nameMsg", "该用户不存在");
            return map;
        }
        if (!password.equals(admin.getPassword())) {
            map.put("passwordMsg", "密码不正确");
            return map;
        }
        return map;
    }
}
