package com.sayo.qa.controller;

import com.sayo.qa.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String name, String password, Model model){
        Map<String,Object> map = customerService.loginCustomer(name,password);
        if (map==null || map.isEmpty()){
            //登录成功
            System.out.println("进入企业用户首页");
            return "/hh/indexCustomer.html";
        }else{
            model.addAttribute("nameMsg",map.get("nameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/hh/loginCustomer.html";
        }
    }

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String registerManager(String name, String password, String email,Model model){
        Map<String, Object> map = customerService.registerCustomer(name,password,email);
        if (map==null || map.isEmpty()){//注册成功，跳转至登录界面
            System.out.println("注册成功");
            return "/hh/loginCustomer.html";
        }else{//重新注册
            model.addAttribute("nameMsg",map.get("nameMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/hh/registerCustomer.html";
        }
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String goLogin(){
        return "/hh/loginCustomer.html";
    }

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String goRegister(){
        return "/hh/registerCustomer.html";
    }




}
