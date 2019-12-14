package com.sayo.qa.controller;

import com.sayo.qa.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;


    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String goRegister(){

        System.out.println("------------------调用我的注册接口了getget");
        return "/register";
    }


    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String goLogin(){
        return "/login";
    }

    @RequestMapping(path = "/registerpost",method = RequestMethod.POST)
    public String registerManager(String name, String password, String email,
                                  int enterpriseId, String type, Model model){
        Map<String, Object> map = managerService.register(name, password, email, enterpriseId, type);
        model.addAttribute("registerMap",map);
        if (map.size()==0 ){//注册成功，跳转至登录界面
            System.out.println("注册成功");
            return "/login.html";
        }else{//重新注册
            for(Map.Entry<String,Object> entry:map.entrySet()){
                     System.out.println(entry.getValue());
            }
            return "/register.html";
        }

    }

    @RequestMapping(path = "/hi",method = RequestMethod.GET)
    public String hi(){
        return "/index";
    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public void login(){

    }


}

