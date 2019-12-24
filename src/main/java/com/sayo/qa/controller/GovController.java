package com.sayo.qa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gov")
public class GovController {
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndex(){
        return "/hh/indexGov";
    }
}
