package com.sayo.qa.controller;

import com.sayo.qa.entity.Inspector;
import com.sayo.qa.service.InspectorService;
import com.sayo.qa.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jws.WebParam;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private InspectorService inspectorService;


    @RequestMapping(path = "/register3User",method = RequestMethod.GET)
    public String goRegister(){
        return "/hh/register.html";
    }


    @RequestMapping(path = "/login3User",method = RequestMethod.GET)
    public String goLogin(){
        return "/hh/login.html";
    }


    //注册3User
    @RequestMapping(path = "/register3User",method = RequestMethod.POST)
    public String registerManager(String name, String password, String email,
                                  int enterpriseId, String role, Model model){
        Map<String, Object> map = managerService.register3User(name, password, email, enterpriseId, role);

        if (map==null || map.isEmpty()){//注册成功，跳转至登录界面
            System.out.println("注册成功");
            return "/hh/login.html";
        }else{//重新注册
            model.addAttribute("nameMsg",map.get("nameMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/hh/register.html";
        }

    }

    @RequestMapping(path = "/hi",method = RequestMethod.GET)
    public String hi(){
        return "/hh/html/index.html";
    }

    @RequestMapping(path = "/hi2",method = RequestMethod.GET)
    public String hi2(){
        return "/hh/html/form_basic.html";
    }

    //第三方登录
    @RequestMapping(path = "/login3User",method = RequestMethod.POST)
    public String login3User(String name, String password, String role, Model model){
        Map<String,Object> map = managerService.loginThirdUser(name,password,role,"第三方");
        if (map.isEmpty() || map==null){
            if ("管理员".equals(role)){//跳转第三方管理员
                System.out.println("进入的的第三方管理员首页");
                return "/index.html";
            }else{
                System.out.println("进入的的第三方质检员首页");
                return "/index.html";
            }
        }else{
            model.addAttribute("nameMsg",map.get("nameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/hh/login.html";
        }
    }

    @RequestMapping(path = "/tablebasic",method = RequestMethod.GET)
    public String tablebasic(){
        return "/hh/html/table_basic.html";
    }

    @RequestMapping(path = "/findInspector",method = RequestMethod.GET)
    public String findInspector(Model model){
        List<Inspector> inspectors = inspectorService.findInspectors();
        System.out.println(inspectors);
        model.addAttribute("inspectors",inspectors);
        return "/hh/html/table_basic.html";
    }

    @RequestMapping(path = "/inspector/detail/{Id}",method = RequestMethod.GET)
    public String InspectorDetail(@PathVariable("Id") int id, Model model){
        Inspector i= inspectorService.findInspectorById(id);
        System.out.println(i);
        model.addAttribute("iDetail",i);
        return "/hh/html/form_basic.html";
    }



}

