package com.sayo.qa.service;

import com.sayo.qa.dao.InspectorMapper;
import com.sayo.qa.dao.ManagerMapper;
import com.sayo.qa.entity.Inspector;
import com.sayo.qa.entity.Manager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ManagerService {
    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    InspectorMapper inspectorMapper;

    public Manager findManagerByName(String name){
        return managerMapper.findManagerByName(name);
    }

    public Manager findManagerById(int id){
        return managerMapper.selectByManagerId(id);
    }

    public Map<String, Object> register3User(String name,String password,String email,int enterpriseId,String role) {
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
        if ("管理员".equals(role)){
            //查看管理员表
            Manager m = managerMapper.selectByManagerName(name,"第三方");
            if (m != null) {
                System.out.println("该账号已经注册"+m);
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
            managerMapper.insertManagerParty(name, password, email, enterpriseId, "第三方");
        }else{
            //查看质检员表
            Inspector inspector = inspectorMapper.selectInspectorByName(name,"第三方");
            if (inspector != null){
                map.put("nameMsg", "该账号已存在!");
                return map;
            }
            inspector = inspectorMapper.selectInspectorByEmail(email);
            if (inspector!=null){
                map.put("emailMsg", "该邮箱已被注册!");
                return map;
            }
            //注册质检员
            inspectorMapper.insertPart(name,password,email,enterpriseId,"第三方");
        }


        return map;
    }

    public Map<String,Object> loginThirdUser(String name,String password,String role,String govOr3){
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
        if ("管理员".equals(role)){
            Manager manager= managerMapper.selectByManagerName(name,govOr3);
            if (manager == null){
                map.put("nameMsg","该用户不存在");
                return map;
            }
            if (!password.equals(manager.getPassword())){
                map.put("passwordMsg","密码不正确");
                return map;
            }
        }else{
            Inspector inspector = inspectorMapper.selectInspectorByName(name,govOr3);
            if (inspector == null){
                map.put("nameMsg","该用户不存在");
                return map;
            }
            if (!password.equals(inspector.getPassword())){
                map.put("passwordMsg","密码不正确");
                return map;
            }
        }

        return map;
    }
    public Map<String,Object> loginGov(String name,String password,String role,String govOr3){
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
        if ("管理员".equals(role)){
            Manager manager= managerMapper.selectByManagerName(name,govOr3);
            if (manager == null){
                map.put("nameMsg","该用户不存在");
                return map;
            }
            if (!password.equals(manager.getPassword())){
                map.put("passwordMsg","密码不正确");
                return map;
            }
        }else{
            Inspector inspector = inspectorMapper.selectInspectorByName(name,govOr3);
            if (inspector == null){
                map.put("nameMsg","该用户不存在");
                return map;
            }
            if (!password.equals(inspector.getPassword())){
                map.put("passwordMsg","密码不正确");
                return map;
            }
        }

        return map;
    }


}
