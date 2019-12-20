package com.sayo.qa.controller;

import com.sayo.qa.entity.Enterprise;
import com.sayo.qa.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jws.WebParam;
import java.util.List;
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
            return "/hh/indexCustomer";
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

    @RequestMapping(path = "/hi",method = RequestMethod.GET)
    public String getForm(){
        return "/hh/form_enterpriseCheck.html";
    }

    @RequestMapping(path = "/eApply",method = RequestMethod.POST)
    public String eApply(Enterprise enterprise, Model model){
        Map<String,Object> map = customerService.apply(enterprise);
        if (map == null || map.isEmpty()){
            //申请中
            return "/hh/indexCustomer";
        }else{
            //已经被申请过了
            model.addAttribute("hasApplied",map.get("hasApplied"));
            return "/hh/indexCustomer";
        }
    }

    /**
     * 已申请的企业信息
     * @param model
     * @return
     */
    @RequestMapping(path = "/searchApplies",method = RequestMethod.GET)
    public String searchApplies(Model model){
        /*这里需要的是customerId，暂时用1测试*/
       List<Map<String,Object>> appliesList = customerService.getApplied(1);
        model.addAttribute("appliesList",appliesList);
        //跳转至认证企业申请列表
        return "/hh/table_applied.html";
    }








}
