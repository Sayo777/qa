package com.sayo.qa.controller;

import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.entity.Enterprise;
import com.sayo.qa.entity.Page;
import com.sayo.qa.service.AdminService;
import com.sayo.qa.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private EnterpriseService enterpriseService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String goLogin() {
        return "/hh/login_admin.html";
    }

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String goIndex(){
        return "/hh/indexAdmin.html";
    }

    /**
     * customer端申请的企业信息审核（enterprise表）
     * @param model
     * @return
     */
    @RequestMapping(path = "/clist",method = RequestMethod.GET)
    public String customerList(Model model, Page page){
        //还未审核的企业
        List<Enterprise> enterpriseList = enterpriseService.findEnterpriseByStatus(0,page.getOffset(),page.getLimit());
        page.setRows(enterpriseService.findEnterpriseRowsByStatus(0));
        page.setPath("/admin/clist");
        model.addAttribute("eList",enterpriseList);
        if (enterpriseList.size()==0){
            model.addAttribute("hint","暂时没有记录");
        }
        return "/hh/table_clist.html";
    }

    @RequestMapping(path = "/eDetail/{id}",method = RequestMethod.GET)
    public String goEdetail(@PathVariable("id") int id,Model model){
        Enterprise enterprise = enterpriseService.getEnterPriseById(id);
        enterprise.setAddress(enterprise.getProvince()+ enterprise.getCity()+ enterprise.getCounty()+enterprise.getAddress());
        model.addAttribute("e",enterprise);
        return "/hh/eDetail.html";
    }


    /**
     * 审核企业信息
     * @param eId 企业id
     * @param status 审核结果 1-通过 0-不通过
     * @return
     */
    @RequestMapping(path = "/checkStatus",method = RequestMethod.POST)
    @ResponseBody
    public String checkStatus(int eId,int status){
        enterpriseService.updateStatusById(eId,status);
        return CommunityUtil.getJSONString(0,"审核通过");
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String name, String password, Model model) {
        Map<String, Object> map = adminService.login(name, password);
        if (map == null || map.size() == 0) {
            return "redirect:/admin/index";
        } else {
            model.addAttribute("nameMsg", map.get("nameMsg"));
            if (map.get("passwordMsg") != null) {
                model.addAttribute("passwordMsg", map.get("passwordMsg"));
                model.addAttribute("name", name);
            }
            return "/hh/login_admin.html";
        }
    }
}

