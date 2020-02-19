package com.sayo.qa.controller;

import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.CommonUtil.HostHolderCustomer;
import com.sayo.qa.entity.ClothesType;
import com.sayo.qa.entity.Customer;
import com.sayo.qa.entity.Enterprise;
import com.sayo.qa.entity.Request;
import com.sayo.qa.service.CustomerService;
import com.sayo.qa.service.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private HostHolderCustomer hostHolderCustomer;
    @Value("/qa")
    private String contextPath;

    @RequestMapping(path = "/hi",method = RequestMethod.GET)
    public String getForm(Model model){
        List<Map<String,Object>> list = customerService.getRequest();
        model.addAttribute("list",list);
        return "/hh/table_request.html";
    }


    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String name, String password, Model model,HttpServletResponse response){
        Map<String,Object> map = customerService.loginCustomer(name,password);
        if (map.containsKey("ticket")){
            // String str = fileUtil.getUrl(key); 上传的文件url
            String aa = map.get("ticket").toString();
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);//cookie范围
            cookie.setMaxAge(3600*12);//cookie有效时间
            response.addCookie(cookie);
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
    public String goLogin(Model model){
//        fileUtil.fileup(model);
        return "/hh/loginCustomer.html";
    }

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String goRegister(){
        return "/hh/registerCustomer.html";
    }

    //页面跳转至信息认证页面
    @RequestMapping(path = "/eApply",method = RequestMethod.GET)
    public String getPageEApply(){
        return "/hh/form_enterpriseCheck.html";
    }


    //申请企业信息认证
    @RequestMapping(path = "/eApply",method = RequestMethod.POST)
    @ResponseBody
    public String eApply(String enterpriseName,String legalPerson,String commercialNumber,String creditCode,String type,
                         String scope,String status,String organCode,String enterpriseEmail,String website,
                         String auditDate,String province,String city,String address,String county,Model model){
        Enterprise e = new Enterprise();
        e.setEnterpriseName(enterpriseName);
        e.setEnterpriseEmail(enterpriseEmail);
        e.setWebsite(website);
        e.setLegalPerson(legalPerson);
        e.setCommercialNumber(commercialNumber);
        e.setCreditCode(creditCode);
        e.setType(type);
        e.setAddress(address);
        e.setProvince(province);
        e.setCity(city);
        e.setCounty(county);
        e.setScope(scope);
        e.setStatus(status);
        e.setOrganCode(organCode);
        e.setAuditDate(DateUtil.stringToDate(auditDate));
        Map<String,Object> map = customerService.apply(e);
        if (map == null || map.isEmpty()){
            return CommunityUtil.getJSONString(0,"申请成功");
        }else{
            //已经被申请过了
            return CommunityUtil.getJSONString(0,"该企业已经认证过了");
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
        Customer c = hostHolderCustomer.getCustomer();
       List<Map<String,Object>> appliesList = customerService.getApplied(c.getId());
        model.addAttribute("appliesList",appliesList);
        //跳转至认证企业申请列表
        return "/hh/table_applied.html";
    }

    //申请质检
    @RequestMapping(path = "/qaReq",method = RequestMethod.POST)
    @ResponseBody
    private String qaReq(String eName,int reqType,String reqName,String contact){
        Map<String, Object> map = customerService.qaReq(eName, reqType, reqName, contact);
        if (map == null || map.size() == 0){
            return CommunityUtil.getJSONString(0,"申请成功！");
        }else{
            if (map.get("reqName")!=null){
                return CommunityUtil.getJSONString(-1,"该申请人不存在");
            }
            return CommunityUtil.getJSONString(-1,"该企业未通过认证");
        }
    }

    //跳转至申请质检的页面
    @RequestMapping(path = "/qaReq",method = RequestMethod.GET)
    public String getFormReqQa(){
        return "/hh/form_reqQa.html";
    }


    @RequestMapping(path = "/hello",method = RequestMethod.GET)
    public void test(){
        Customer customer = hostHolderCustomer.getCustomer();

        return;
    }

    @RequestMapping(path = "/getReqrecord",method = RequestMethod.GET)
    public String getReqrecord(Model model){
        List<Map<String,Object>> list = customerService.getReqrecord();
        model.addAttribute("list",list);
        return "/hh/table_reqResult.html";
    }










}
