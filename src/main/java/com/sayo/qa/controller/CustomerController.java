package com.sayo.qa.controller;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.CommonUtil.HostHolderCustomer;
import com.sayo.qa.entity.*;
import com.sayo.qa.event.EventPro;
import com.sayo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private EnterpriseService enterpriseService;
    @Value("/qa")
    private String contextPath;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private InspectorService inspectorService;
    @Autowired
    private MessageService messageService;

    //通知
    @RequestMapping(path = "/myNotify", method = RequestMethod.GET)
    public String myNotify(Model model, HttpServletRequest request) {
        //点击了之后消息1就没有了


        int customerId = (int) request.getSession().getAttribute("loginCustomer");
        Message message = messageService.findMessageByToandStatus(customerId, 0);
        model.addAttribute("msg", message);
        model.addAttribute("type", message.getType());
        if (message.getType() == "告知单确认") {
            model.addAttribute("content", "您好，您申请的质检已通过审核，请尽快至《申请记录》中进行告知单确认！");
            model.addAttribute("aa", "/qa/customer/hi");
        } else {
            model.addAttribute("content", "您好，质检员的《抽样单》已送达，请尽快完成确认！");
            //抽样单确认
            model.addAttribute("aa", "/qa/customer/hi");
        }
        message.setStatus(1);
        messageService.updateMessage(message);


        return "/hh/customer/notify.html";
    }

    //已完成的任务列表
    @RequestMapping(path = "/finishedTask", method = RequestMethod.GET)
    public String finishedTask(HttpServletRequest request, Model model, Page page) {
        Customer customer = customerService.findCustomerById((int) request.getSession().getAttribute("loginCustomer"));
        page.setRows(taskService.findFinishedTaskByEidRows(customer.getEnterpriseId()));
        page.setPath("/customer/finishedTask");
        List<Task> taskList = taskService.findFinishedTaskByEid(customer.getEnterpriseId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> taskVoList = new ArrayList<>();
        if (taskList != null) {
            for (Task task : taskList) {
                Map<String, Object> taskVo = new HashMap<>();
                taskVo.put("task", task);
                taskVo.put("startTime", DateUtil.dateToString(task.getStartTime().toString()));
                taskVo.put("reqEnterprise", enterpriseService.getEnterPriseById(requestService.getRequest(task.getTaskId()).getReqEid()));
                taskVo.put("inspector1", inspectorService.findInspectorById(task.getInspectorOne()));
                taskVo.put("inspector2", inspectorService.findInspectorById(task.getInspectorTwo()));
                taskVoList.add(taskVo);
            }
        }
        if (taskVoList.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }else{

            model.addAttribute("tasks", taskVoList);
        }
        return "/hh/customer/finishedTask.html";
    }

    @RequestMapping(path = "/productList", method = RequestMethod.GET)
    public String productList(Model model, HttpServletRequest request, Page page) {

        Customer customer = customerService.findCustomerById((int) request.getSession().getAttribute("loginCustomer"));
        page.setRows(productService.findProductRows(customer.getEnterpriseId()));
        page.setPath("/customer/productList");
        List<Product> list = productService.findProductByEid(customer.getEnterpriseId(), page.getOffset(), page.getLimit());
        model.addAttribute("productList", list);
        if (list.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }
        return "/hh/customer/table_product.html";
    }

    @RequestMapping(path = "/delProduct", method = RequestMethod.POST)
    @ResponseBody
    public String delProduct(int pid) {
        productService.delProductById(pid);
        return CommunityUtil.getJSONString(0, "删除成功");
    }

    @RequestMapping(path = "/toProductList", method = RequestMethod.GET)
    public String toProductList() {
        return "redirect:/customer/productList";
    }

    @Autowired
    private SampleService sampleService;

    @RequestMapping(path = "/findSampleById", method = RequestMethod.POST)
    @ResponseBody
    public String findSampleById(int id) {
        Sample sample = sampleService.findSampleByTaskId(id);
        Map<String, Object> map = new HashMap<>();
        map.put("sample", sample);
        return CommunityUtil.getJSONString(0, "success", map);
    }

    @RequestMapping(path = "/sampleSure", method = RequestMethod.POST)
    @ResponseBody
    public String sampleSure(int reqId) {
        int i = sampleService.updateStatusById(reqId);
        if (i<0){
            return CommunityUtil.getJSONString(-1, "确认失败了！");
        }else{
            return CommunityUtil.getJSONString(0, "确认成功！");
        }
    }


    @RequestMapping(path = "/hi", method = RequestMethod.GET)
    public String getForm(Model model, HttpServletRequest request, Page page) {
        Customer customer = customerService.findCustomerById((int) request.getSession().getAttribute("loginCustomer"));
        List<Map<String, Object>> list = customerService.getRequest(customer, page.getOffset(), page.getLimit());
        if(list.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }else{
            model.addAttribute("list", list);
        }
        page.setRows(requestService.findReqRowByEid(customer.getEnterpriseId()));
        page.setPath("/customer/hi");
        return "/hh/table_request.html";
    }

    @Autowired
    private ReqArrangeService reqArrangeService;

    //告知书确认
    @RequestMapping(path = "/gaozhishuSure", method = RequestMethod.POST)
    @ResponseBody
    public String gaozhishuSure(String reqId) {
        int id = Integer.parseInt(reqId);
        int i = reqArrangeService.updateGaozhishu(id);
        if (i < 0) {
            return CommunityUtil.getJSONString(1, "确认出错啦！！");
        } else {
            return CommunityUtil.getJSONString(0, "确认成功！请等待质检安排");
        }
    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String name, String password, Model model, HttpServletRequest request) {
        Map<String, Object> map = customerService.loginCustomer(name, password);
        if (map == null || map.size() == 0) {
            Customer logincustomer = customerService.findCustomerByName(name);
            request.getSession().setAttribute("loginCustomer", logincustomer.getId());
            return "redirect:/customer/indexCustomer";
        } else {
            model.addAttribute("nameMsg", map.get("nameMsg"));
            if (map.get("passwordMsg") != null) {
                model.addAttribute("passwordMsg", map.get("passwordMsg"));
                model.addAttribute("name", name);
            }
            return "/hh/customer/loginCustomer.html";

        }
    }

    @RequestMapping(path = "/indexCustomer", method = RequestMethod.GET)
    public String toIndex(Model model, HttpServletRequest request) {
        int customerId = (int) request.getSession().getAttribute("loginCustomer");
        Customer customer = customerService.findCustomerById(customerId);
        model.addAttribute("customerName", customer.getName());
        int notifyCount = messageService.findCountByToId(customerId);
        if (notifyCount != 0) {
            model.addAttribute("notifyCount", notifyCount);
        }
        return "/hh/indexCustomer.html";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String registerManager(String name, String password, String email, Model model) {
        Map<String, Object> map = customerService.registerCustomer(name, password, email);
        if (map == null || map.isEmpty()) {//注册成功，跳转至登录界面
            return "redirect:/customer/login";
        } else {//重新注册
            model.addAttribute("nameMsg", map.get("nameMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/hh/registerCustomer.html";
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String goLogin() {
        return "/hh/customer/loginCustomer.html";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String goRegister() {

        return "/hh/registerCustomer.html";
    }


    @Value("${qiniu.bucket.file.name}")
    private String headerBucketName;

    //页面跳转至信息认证页面
    @RequestMapping(path = "/eApply", method = RequestMethod.GET)
    public String getPageEApply(Model model, HttpServletRequest request) {
        int customerId = (int) request.getSession().getAttribute("loginCustomer");
        int status = enterpriseService.findEnterpriseStatusByCustomerId(customerId);
        if (status == 1) {  //当前用户的企业已经通过审核了
            return "/hh/customer/hasApplied.html";
        }

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
    @RequestMapping(path = "/eApply", method = RequestMethod.POST)
    public String eApply(String enterpriseName, String legalPerson, String commercialNumber, String
            creditCode, String type,
                         String scope, String status, String organCode, String enterpriseEmail, String website,
                         String auditDate, String s_province, String s_city, String address, String s_county, String key,
                         Model model, HttpServletRequest request) {
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
        e.setLicenseSrc("http://q5qcypm7u.bkt.clouddn.com/" + key);
        Map<String, Object> map = customerService.apply(e, request);
        int customerId = (int) request.getSession().getAttribute("loginCustomer");
        if (map == null || map.isEmpty()) {
            int eId = enterpriseService.getEnterPriseByName(enterpriseName).getId();
            customerService.updateCustomerEId(customerId, eId);
            model.addAttribute("apply_result", "申请成功！");
        } else {
            model.addAttribute("apply_result", "该企业已经被申请过了！");
        }
        return "/hh/waiting.html";
    }

    /**
     * 已申请的企业信息
     *
     * @param model
     * @return
     */
    @RequestMapping(path = "/searchApplies", method = RequestMethod.GET)
    public String searchApplies(Model model, HttpServletRequest request) {
        int customerId = (int) request.getSession().getAttribute("loginCustomer");
        int eId = customerService.findCustomerById(customerId).getEnterpriseId();

//        List<Map<String, Object>> appliesList = customerService.getApplied(customerId);
//        model.addAttribute("appliesList", appliesList);
//        if (appliesList.size()==0){
//            model.addAttribute("hint","暂时没有记录");
//        }
//        //跳转至认证企业申请列表
//        return "/hh/table_applied.html";
        return "redirect:/customer/inf/"+eId;
    }

    @Autowired
    private ThirdqaService thirdqaService;
    @RequestMapping(path = "/inf/{id}",method = RequestMethod.GET)
    public String toInf(@PathVariable("id") int id,Model model){
        Thirdqa thirdqa = thirdqaService.findThirdqaById(id);
        model.addAttribute("t",thirdqa);
        return "/hh/customer/thirdqaInf.html";
    }
//
//    @Autowired
//    private EventPro eventPro;

    //申请质检
    @RequestMapping(path = "/qaReq", method = RequestMethod.POST)
    public String qaReq1(String eName, int reqType, String reqName, String contact, int productId, Model model,HttpServletRequest request) {
        int customerId = (int) request.getSession().getAttribute("loginCustomer");
        Map<String, Object> map = customerService.qaReq(eName, reqType, reqName, contact, productId);
        if (map == null || map.size() == 0) {
            Event event = new Event();
            event.setTopic("req")
                    .setUserId(customerId)
                    .setEntityuserId(0)
                    .setEntityType("质检申请");
//            eventPro.fireEvent(event);


            model.addAttribute("msg", "申请成功");


        } else {
            model.addAttribute("msg", "申请失败，请检查表单填写！！");
        }
        return "/hh/reqWaiting.html";
    }


    //跳转至申请质检的页面
    @RequestMapping(path = "/qaReq", method = RequestMethod.GET)
    public String getFormReqQa(Model model, HttpServletRequest request) {
        int customerId = (int) request.getSession().getAttribute("loginCustomer");
        int eid = customerService.findCustomerById(customerId).getEnterpriseId();
        Customer customer = customerService.findCustomerById(customerId);
        model.addAttribute("cName",customer.getName());
        model.addAttribute("tel",customer.getPhone());
        Enterprise enterprise = enterpriseService.getEnterPriseById(eid);
        model.addAttribute("eName", enterprise.getEnterpriseName());
        List<Product> productList = productService.findProductByEid1(eid);
        model.addAttribute("productList", productList);
        return "/hh/form_reqQa.html";
    }


    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public void test() {
        Customer customer = hostHolderCustomer.getCustomer();

        return;
    }

    @RequestMapping(path = "/getReqrecord", method = RequestMethod.GET)
    public String getReqrecord(Model model) {
        List<Map<String, Object>> list = customerService.getReqrecord();
        model.addAttribute("list", list);
        if (list.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }
        return "/hh/table_reqResult.html";
    }


    @RequestMapping(path = "/addProduct", method = RequestMethod.GET)
    public String toAddproduct() {
        return "/hh/customer/add_product.html";
    }

    @Autowired
    private ProductService productService;

    @RequestMapping(path = "/addProduct", method = RequestMethod.POST)
    public String addProduct(Model model, String productName, String guige, String standard,
                             String shichang, String manufacture, HttpServletRequest request) {
        int customerId = (int) request.getSession().getAttribute("loginCustomer");
        Customer customer = customerService.findCustomerById(customerId);
        int enterpriseId = customer.getEnterpriseId();
        Product product = new Product();
        product.setEid(enterpriseId);
        product.setProductName(productName);
        product.setStandard(standard);
        product.setGuige(guige);
        product.setShichang(shichang);
        product.setManufacture(manufacture);
        //加之前看看该企业存在该产品吗（相同名称+规格）
        Product same = productService.findSameProduct(enterpriseId, productName, guige);
        if (same == null) {
            productService.addProduct(product);
            model.addAttribute("msg", "添加成功");
        } else {
            model.addAttribute("msg", "该产品已经添加了！！");
        }
        return "/hh/customer/add_product.html";
    }


    @RequestMapping(path = "/gaozhishu", method = RequestMethod.GET)
    public String gaozhishu() {
        return "/hh/customer/gaozhishu.html";
    }


}

