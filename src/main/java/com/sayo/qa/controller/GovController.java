package com.sayo.qa.controller;

import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.entity.*;
import com.sayo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/gov")
public class GovController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private ThirdTaskService thirdTaskService;
    @Autowired
    private InspectorService inspectorService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private TaskCheckService taskCheckService;
    @Autowired
    private ProductService productService;

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String toLogin(){
        return "/hh/gov/loginGov.html";
    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String loginGov(String name, String password, String role, Model model, HttpServletRequest request){
        Map<String,Object> map = managerService.loginGov(name,password,role,"政府");
        if (map.isEmpty() || map==null){
            if ("管理员".equals(role)){//跳转第三方管理员
                request.getSession().setAttribute("loginGovManager",name);
                return "/hh/indexGov.html";
            }else{
                return "/hh/indexInspector.html";
            }
        }else{
            model.addAttribute("nameMsg",map.get("nameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/hh/gov/loginGov.html";
        }
    }

    @RequestMapping(path = "/insYes",method = RequestMethod.POST)
    @ResponseBody
    public String insYes(int taskId){
        //更新task表、thirdTask表
        ThirdTask thirdTask = thirdTaskService.findThirdTaskById(taskId);
        thirdTaskService.updateStatusByTaskIdAndCode(3,taskId);
        Task task = new Task();
        task.setTaskId(taskId);
        task.setInspectorOne(thirdTask.getIns1());
        task.setInspectorTwo(thirdTask.getIns2());

        int i = taskService.updateTaskSelective(task);
        if (i<0){
            return CommunityUtil.getJSONString(1,"审核出错啦！");
        }else{
            return CommunityUtil.getJSONString(0,"审核成功！");
        }
    }

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndex(){
        return "/hh/indexGov";
    }

    @RequestMapping(value = "/checkIns",method = RequestMethod.GET)
    public String toCheckIns(Model model){
        List<Task> task = taskService.findTaskforCheckIns();
        List<Map<String,Object>> VoList = new ArrayList<>();
        Map<String,Object> map = null;
        for (Task t: task) {
            map = new HashMap<>();
            map.put("VoTask",getVoTaskByTask(t)); // 任务编号、受检企业、申请人、联系电话、受检服装类型、受检时间、地址
            ThirdTask thirdTask = thirdTaskService.findThirdTaskById(t.getTaskId());
            map.put("inspector1",inspectorService.findInspectorById(thirdTask.getIns1()));
            map.put("inspector2",inspectorService.findInspectorById(thirdTask.getIns2()));
            VoList.add(map);
        }
        model.addAttribute("VoList",VoList);

        return "/hh/gov/check_ins.html";
    }

    //已完成任务列表
    @RequestMapping(path = "/finishedTask",method = RequestMethod.GET)
    public String finishedTask(HttpServletRequest request, Model model,Page page){
        //用request.getSession()来获取当前登录的人员，先暂时用质检员id=2的来展示
//        String inspectorName = (String)request.getSession().getAttribute("inspectorName");
//        Inspector inspector = inspectorService.findInspectorByName(inspectorName);
        List<Task> taskList = taskService.findFinishTaskAll(page.getOffset(),page.getLimit());
        page.setRows(taskService.findFinishTaskAllRows());
        page.setPath("/gov/finishedTask");
        List<Map<String,Object>> taskVoList = new ArrayList<>();
        if (taskList!=null){
            for (Task task:taskList) {
                Map<String,Object> taskVo = new HashMap<>();
                taskVo.put("task",task);
                taskVo.put("startTime", DateUtil.dateToString(task.getStartTime().toString()));
                taskVo.put("reqEnterprise",enterpriseService.getEnterPriseById(requestService.getRequest(task.getTaskId()).getReqEid()));
                taskVo.put("inspector1",inspectorService.findInspectorById(task.getInspectorOne()));
                taskVo.put("inspector2",inspectorService.findInspectorById(task.getInspectorTwo()));
                taskVoList.add(taskVo);
            }
        }
        model.addAttribute("tasks",taskVoList);
        return "/hh/gov/finishedTask.html";
    }

    //已拒绝的任务
    @RequestMapping(path = "/refuseTask",method = RequestMethod.GET)
    public String refuseTask(HttpServletRequest request, Model model,Page page){
        List<Request> refuseTask = requestService.findRefuseTask(page.getOffset(),page.getLimit());
        page.setRows(requestService.findRefuseTaskRows());
        page.setPath("/gov/refuseTask");
        List<Map<String,Object>> refuseList = new ArrayList<>();
        if (refuseTask!=null){
            for (Request r:refuseTask) {
                Map<String,Object> map = new HashMap<>();
                TaskCheck taskCheck = taskCheckService.findTaskCheckByReqId(r.getId());
                Customer customer = customerService.findCustomerById(r.getReqId());
                String clothesType = clothesTypeService.getClothesType(r.getReqType());
                Product product = productService.findProductById(r.getProductId());
                map.put("r",r);
                map.put("taskcheck",taskCheck);
                map.put("customer",customer.getName());
                map.put("clothesType",clothesType);
                map.put("product",product.getProductName());
                refuseList.add(map);
            }
        }
        model.addAttribute("refuseList",refuseList);
        return "/hh/gov/refuseTask.html";
    }

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ClothesTypeService clothesTypeService;
    public VoTask getVoTaskByTask(Task task){
        Request request = requestService.getRequest(task.getTaskId());
        Enterprise enterprise = enterpriseService.getEnterPriseById(task.getQaEid());
        Customer customer = customerService.findCustomerById(request.getReqId());
        VoTask voTask = new VoTask();
        String address = enterprise.getProvince()+enterprise.getCity()+enterprise.getCounty()+enterprise.getAddress();
        voTask.setTaskId(task.getTaskId());
        voTask.setQaEnterprise(enterprise);
        voTask.setQaCustomer(customer);
        voTask.setContact(request.getContact());
        voTask.setClothType(clothesTypeService.getClothesType(request.getReqType()));
        voTask.setStartTime(task.getStartTime());
        voTask.setAddress(address);
        return voTask;
    }
}
