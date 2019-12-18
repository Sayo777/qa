package com.sayo.qa.service;

import com.sayo.qa.dao.CustomerMapper;
import com.sayo.qa.entity.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    public Map<String, Object> registerCustomer(String name, String password, String email) {
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

            //查看Customer表
            Customer customer =customerMapper.selectCustomerByName(name);
            if (customer != null) {
                System.out.println("该账号已经注册"+customer.getName());
                map.put("nameMsg", "该账号已存在!");
                return map;
            }
            // 验证邮箱
            customer = customerMapper.selectCustomerByEmail(email);
            if (customer != null) {
                map.put("emailMsg", "该邮箱已被注册!");
                return map;
            }
            Customer c = new Customer();
            c.setName(name);
            c.setPassword(password);
            c.setEmail(email);
            //注册企业用户
            customerMapper.insertSelective(c);
        return map;
    }

    public Map<String,Object> loginCustomer(String name,String password){
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

            Customer customer = customerMapper.selectCustomerByName(name);
            if (customer == null){
                map.put("nameMsg","该用户不存在");
                return map;
            }
            if (!password.equals(customer.getPassword())){
                map.put("passwordMsg","密码不正确");
                return map;
            }
        return map;
    }
}
