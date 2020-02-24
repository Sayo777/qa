package com.sayo.qa.controller;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.CommonUtil.HostHolderCustomer;
import com.sayo.qa.entity.*;
import com.sayo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private ReqArrangeService reqArrangeService;
    //告知书确认
    @RequestMapping(path = "/gaozhishuSure",method = RequestMethod.POST)
    @ResponseBody
    public String gaozhishuSure(String reqId){
        int id = Integer.parseInt(reqId);
        int i = reqArrangeService.updateGaozhishu(id);
        if (i<0){
            return CommunityUtil.getJSONString(1,"确认出错啦！！");
        }else{
            return CommunityUtil.getJSONString(0,"确认成功！请等待质检安排");
        }
    }


    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String name, String password, Model model, HttpServletResponse response, HttpServletRequest request){
        Map<String,Object> map = customerService.loginCustomer(name,password);
        if (map.containsKey("ticket")){
            String aa = map.get("ticket").toString();
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);//cookie范围
            cookie.setMaxAge(3600*12);//cookie有效时间
            response.addCookie(cookie);
            Customer customer = customerService.findCustomerByName(name);
            request.getSession().setAttribute("customerName",name);
            request.getSession().setAttribute("enterpriseId",customer.getEnterpriseId());
            model.addAttribute("customerName",name);
            return "/hh/indexCustomer";
        }else{
            model.addAttribute("nameMsg",map.get("nameMsg"));
            if(map.get("passwordMsg")!=null){
                model.addAttribute("passwordMsg",map.get("passwordMsg"));
                model.addAttribute("name",name);
            }

            return "/hh/customer/loginCustomer.html";
        }
    }

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String registerManager(String name, String password, String email,Model model){
        Map<String, Object> map = customerService.registerCustomer(name,password,email);
        if (map==null || map.isEmpty()){//注册成功，跳转至登录界面
            return "redirect:/customer/login";
        }else{//重新注册
            model.addAttribute("nameMsg",map.get("nameMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/hh/registerCustomer.html";
        }
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String goLogin(Model model){
        return "/hh/customer/loginCustomer.html";
    }

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String goRegister(){

        return "/hh/registerCustomer.html";
    }


    @Value("${qiniu.bucket.file.name}")
    private String headerBucketName;
    //页面跳转至信息认证页面
    @RequestMapping(path = "/eApply",method = RequestMethod.GET)
    public String getPageEApply(Model model){
        String accessKey = "vaajrANXR3GmkHhZgEuYORNs1b30AJMNIA1vyRo8";
        String secretKey = "kus8Sfpg5AB-34jwGr290_pgs2Rc7o8Y7VHpVNF0";
        String headerBucketName = "sayo-finish";
        String fileName = CommunityUtil.generateUUID();
        // 设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));
        // 生成上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);

        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);
        return "/hh/form_enterpriseCheck.html";
    }


    //申请企业信息认证
    @RequestMapping(path = "/eApply",method = RequestMethod.POST)
    public String eApply(String enterpriseName,String legalPerson,String commercialNumber,String creditCode,String type,
                         String scope,String status,String organCode,String enterpriseEmail,String website,
                         String auditDate,String s_province,String s_city,String address,String s_county,String key,
                         Model model,HttpServletRequest request){
        Enterprise e = new Enterprise();
        e.setEnterpriseName(enterpriseName);
        e.setEnterpriseEmail(enterpriseEmail);
        e.setWebsite(website);
        e.setLegalPerson(legalPerson);
        e.setCommercialNumber(commercialNumber);
        e.setCreditCode(creditCode);
        e.setType(type);
        e.setAddress(address);
        e.setProvince(s_province);
        e.setCity(s_city);
        e.setCounty(s_county);
        e.setScope(scope);
        e.setStatus(status);
        e.setOrganCode(organCode);
        e.setAuditDate(DateUtil.stringToDate(auditDate));
        e.setLicenseSrc("http://q5qcypm7u.bkt.clouddn.com/"+key);
        Map<String,Object> map = customerService.apply(e,request);
        if (map == null || map.isEmpty()){
            model.addAttribute("apply_result","申请成功！");
        }else {
            model.addAttribute("apply_result", "该企业已经被申请过了！");
        }
        return "/hh/waiting.html";
    }

    /**
     * 已申请的企业信息
     * @param model
     * @return
     */
    @RequestMapping(path = "/searchApplies",method = RequestMethod.GET)
    public String searchApplies(Model model,HttpServletRequest request){
        String customerName =(String) request.getSession().getAttribute("customerName");
        Customer c = customerService.findCustomerByName(customerName);
       List<Map<String,Object>> appliesList = customerService.getApplied(c.getId());
        model.addAttribute("appliesList",appliesList);
        //跳转至认证企业申请列表
        return "/hh/table_applied.html";
    }

    //申请质检
    @RequestMapping(path = "/qaReq",method = RequestMethod.POST)
    public String qaReq1(String eName,int reqType,String reqName,String contact,int productId, Model model){
        Map<String, Object> map = customerService.qaReq(eName, reqType, reqName, contact,productId);

        if (map == null || map.size() == 0){
            model.addAttribute("msg","申请成功");
        }else{
            model.addAttribute("msg","申请失败，请检查表单填写！！");
        }
        return "/hh/reqWaiting.html";
    }

    @Autowired
    private EnterpriseService enterpriseService;

    //跳转至申请质检的页面
    @RequestMapping(path = "/qaReq",method = RequestMethod.GET)
    public String getFormReqQa(Model model,HttpServletRequest request,HttpServletResponse response){
        String customerName = (String) request.getSession().getAttribute("customerName");
        int eid = customerService.findCustomerByName(customerName).getEnterpriseId();
        Enterprise enterprise = enterpriseService.getEnterPriseById(eid);
        model.addAttribute("eName",enterprise.getEnterpriseName());
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


    @RequestMapping(path = "/addProduct",method = RequestMethod.GET)
    public String toAddproduct(){
        return "/hh/customer/add_product.html";
    }

    @Autowired
    private ProductService productService;

    @RequestMapping(path = "/addProduct",method = RequestMethod.POST)
    public String addProduct(Model model,String productName,String guige,String standard,
                             String shichang,String manufacture,HttpServletRequest request){
        String customerName = (String) request.getSession().getAttribute("customerName");
        Customer customer = customerService.findCustomerByName(customerName);
        int enterpriseId = customer.getEnterpriseId();
        Product product = new Product();
        product.setEid(enterpriseId);
        product.setProductName(productName);
        product.setStandard(standard);
        product.setGuige(guige);
        product.setShichang(shichang);
        product.setManufacture(manufacture);
        //加之前看看该企业存在该产品吗（相同名称+规格）
        Product same = productService.findSameProduct(enterpriseId,productName,guige);
        if (same == null){
            productService.addProduct(product);
            model.addAttribute("msg","添加成功");
        }else{
            model.addAttribute("msg","该产品已经添加了！！");
        }
        return "/hh/customer/add_product.html";
    }


    @RequestMapping(path = "/gaozhishu",method = RequestMethod.GET)
    public String gaozhishu(){
        return "/hh/customer/gaozhishu.html";
    }





}
