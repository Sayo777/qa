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

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String registerManager(String name, String password, String email,
                                  int enterpriseId, String type, Model model){
        Map<String, Object> map = managerService.register(name, password, email, enterpriseId, type);
        model.addAttribute("registerMap",map);
        return "/login.html";
    }

    @RequestMapping(path = "/hi",method = RequestMethod.GET)
    public String hi(){
        return "/login";
    }


}

