package com.sayo.qa.controller;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.dao.TaskMapper;
import com.sayo.qa.entity.*;
import com.sayo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/3User")
public class ThirdUserController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private RequestService requestService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private InspectorService inspectorService;
    @Autowired
    private ThirdTaskService thirdTaskService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ThirdqaService thirdqaService;


    //申请企业信息认证
    @RequestMapping(path = "/eApply", method = RequestMethod.POST)
    public String eApply(String thirdName, String legalPerson, String commercialNumber, String
            creditCode, String type,
                         String scope, String status, String organCode, String thirdEmail, String contactPerson, String tel,
                         String auditDate, String s_province, String s_city, String address,
                         Model model) {
        Thirdqa t = new Thirdqa();
        t.setThirdName(thirdName);
        t.setContactPerson(contactPerson);
        t.setTel(tel);
        t.setThirdEmail(thirdEmail);
        t.setLegalPerson(legalPerson);
        t.setCommercialNumber(Integer.parseInt(commercialNumber));
        t.setCreditCode(creditCode);
        t.setType(type);
        t.setAddress(address);
        t.setProvince(s_province);
        t.setCity(s_city);
        t.setScope(scope);
        t.setStatus(status);
        t.setOrganCode(organCode);
        t.setAuditDate(DateUtil.stringToDate(auditDate));
        Map<String, Object> map = thirdqaService.apply(t);
        if (map == null || map.isEmpty()) {
            model.addAttribute("apply_result", "申请成功！");
        } else {
            model.addAttribute("apply_result", "该企业已经被申请过了！");
        }
        return "/hh/third/waiting.html";
    }

    @RequestMapping(path = "/eApply", method = RequestMethod.GET)
    public String getPageEApply(HttpServletRequest request) {
        String managerName = (String) request.getSession().getAttribute("loginManager");
        if (managerName == null) {
            return "redirect:/3User/index";
        }
        Thirdqa thirdqa = thirdqaService.findThirdqaById(managerService.findManagerByName(managerName).getEnterpriseId());
        if (thirdqa.getNotes().equals("通过")) {  //当前用户的企业已经通过审核了
            return "/hh/third/hasApplied.html";
        }
        return "/hh/third/form_enterpriseCheck.html";
    }

    /**
     * 已申请的企业信息
     *
     * @param model
     * @return
     */
    @RequestMapping(path = "/searchApplies", method = RequestMethod.GET)
    public String searchApplies(Model model, HttpServletRequest request) {
        String managerName = (String) request.getSession().getAttribute("loginManager");
        if (managerName == null) {
            return "redirect:/3User/index";
        }
        Thirdqa thirdqa = thirdqaService.findThirdqaById(managerService.findManagerByName(managerName).getEnterpriseId());
        thirdqa.setAddress(thirdqa.getProvince() + thirdqa.getCity() + thirdqa.getAddress());
        if (thirdqa.getNotes().equals("未通过")) {
            model.addAttribute("hint", "（待审核中...）");
        } else {
            model.addAttribute("hint", "（已过审）");
        }
        model.addAttribute("t", thirdqa);
        return "/hh/third/thirdqaInf.html";
    }

    //已完成的任务列表
    @RequestMapping(path = "/finishTask", method = RequestMethod.GET)
    public String finishedTask(HttpServletRequest request, Model model, Page page) {
        Manager manager = managerService.findManagerByName((String) request.getSession().getAttribute("loginManager"));
        page.setRows(taskService.findRowsFinishedTaskByQaEid(manager.getEnterpriseId()));
        page.setPath("/3User/finishTask");
        List<Task> taskList = taskService.findFinishedTaskByQaEid(manager.getEnterpriseId(), page.getOffset(), page.getLimit());
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
        if (taskVoList.size() == 0) {
            model.addAttribute("hint", "暂时没有记录！");
        }
        model.addAttribute("tasks", taskVoList);
        return "/hh/third/finishedTask.html";
    }


    @RequestMapping(path = "/myArrange", method = RequestMethod.POST)
    @ResponseBody
    public String myArrange(int taskId) {
        int i = thirdTaskService.updateThirdTaskStatus(taskId);
        if (i < 0) {
            return CommunityUtil.getJSONString(1, "安排任务出错啦！");
        } else {
            return CommunityUtil.getJSONString(0, "安排成功！");
        }
    }


    @RequestMapping(path = "/sendIns", method = RequestMethod.POST)
    @ResponseBody
    public String sendIns(int taskId, String ins1, String ins2) {
        Inspector inspector1 = inspectorService.findInspectorByName(ins1);
        Inspector inspector2 = inspectorService.findInspectorByName(ins2);
        int i = thirdTaskService.updateThirdTaskIns(taskId, inspector1.getId(), inspector2.getId());
        if (i < 0) {
            return CommunityUtil.getJSONString(1, "安排任务出错啦！");
        } else {
            return CommunityUtil.getJSONString(0, "已安排！待政府审核。。。");
        }
    }

    @RequestMapping(path = "/hasAssign", method = RequestMethod.GET)
    public String getUnfinishTask(Model model,Page page,HttpServletRequest request) {
        Manager manager = managerService.findManagerByName((String) request.getSession().getAttribute("loginManager"));
        List<Task> task = taskService.findhasAssignTaskByEid(manager.getEnterpriseId(),page.getOffset(),page.getLimit());
        page.setRows(taskService.findRowshasAssignTaskByEid(manager.getEnterpriseId()));
        page.setPath("/3User/hasAssign");
        List<Map<String, Object>> VoList = new ArrayList<>();
        Map<String, Object> map = null;
        for (Task t : task) {
            map = new HashMap<>();
            map.put("VoTask", getVoTaskByTask(t)); // 任务编号、受检企业、申请人、联系电话、受检服装类型、受检时间、地址
            map.put("inspector1", inspectorService.findInspectorById(t.getInspectorOne()));
            map.put("inspector2", inspectorService.findInspectorById(t.getInspectorTwo()));
            VoList.add(map);
        }
        if (VoList.size() == 0) {
            model.addAttribute("hint", "暂时没有记录！");
        } else {
            model.addAttribute("VoList", VoList);
        }
        return "/hh/third/hasAssign.html";
    }

    @RequestMapping(path = "/finishAssign", method = RequestMethod.GET)
    public String getfinishTask(Model model, HttpServletRequest request,Page page) {

        Manager manager = managerService.findManagerByName((String) request.getSession().getAttribute("loginManager"));
        //找出政府审核通过的task（status=3）
        List<Task> task = taskService.findfinishAssignTaskByEidAndStatus(manager.getEnterpriseId(), 3,page.getOffset(),page.getLimit());
        page.setPath("/3User/finishAssign");
        page.setRows(taskService.findRowsfinishAssignTaskByEidAndStatus(manager.getEnterpriseId(), 3));
        List<Map<String, Object>> VoList = new ArrayList<>();
        Map<String, Object> map = null;
        for (Task t : task) {
            map = new HashMap<>();
            map.put("VoTask", getVoTaskByTask(t)); // 任务编号、受检企业、申请人、联系电话、受检服装类型、受检时间、地址
            map.put("inspector1", inspectorService.findInspectorById(t.getInspectorOne()));
            map.put("inspector2", inspectorService.findInspectorById(t.getInspectorTwo()));
            VoList.add(map);
        }
        model.addAttribute("VoList", VoList);
        if (VoList.size() == 0) {
            model.addAttribute("hint", "暂时没有记录！");
        }
        return "/hh/third/finishAssign.html";
    }

    @RequestMapping(path = "/waitingAssign", method = RequestMethod.GET)
    public String towaitingAssign(Model model, HttpServletRequest request) {
        Manager manager = managerService.findManagerByName((String) request.getSession().getAttribute("loginManager"));
        List<Task> task = taskService.findwaitingAssignTaskByEid(manager.getEnterpriseId());
        List<Map<String, Object>> VoList = new ArrayList<>();
        Map<String, Object> map = null;
        for (Task t : task) {
            map = new HashMap<>();
            map.put("VoTask", getVoTaskByTask(t)); // 任务编号、受检企业、申请人、联系电话、受检服装类型、受检时间、地址
            VoList.add(map);
        }
        model.addAttribute("VoList", VoList);
        //根据manager找到eid
        int eid = manager.getEnterpriseId();
        List<Inspector> inspectorList = inspectorService.findInspectorByqa3AndType(eid,"第三方");
        model.addAttribute("list",inspectorList);
        if (VoList.size() == 0) {
            model.addAttribute("hint", "暂时没有记录！");
        }
        return "/hh/third/waitingAssign.html";
    }


    @Autowired
    private CustomerService customerService;
    @Autowired
    private ClothesTypeService clothesTypeService;

    public VoTask getVoTaskByTask(Task task) {
        Request request = requestService.getRequest(task.getTaskId());
        Enterprise enterprise = enterpriseService.getEnterPriseById(task.getQaEid());
        Customer customer = customerService.findCustomerById(request.getReqId());
        VoTask voTask = new VoTask();
        String address = enterprise.getProvince() + enterprise.getCity() + enterprise.getCounty() + enterprise.getAddress();
        voTask.setTaskId(task.getTaskId());
        voTask.setQaEnterprise(enterprise);
        voTask.setQaCustomer(customer);
        voTask.setContact(request.getContact());
        voTask.setClothType(clothesTypeService.getClothesType(request.getReqType()));
        voTask.setStartTime(task.getStartTime());
        voTask.setAddress(address);
        return voTask;
    }


    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login3User(String name, String password, String role, Model model) {
        return "";
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index() {
        return "/hh/login.html";
    }

    @RequestMapping(path = "/inspector/detail", method = RequestMethod.GET)
    public String getDetailinspector() {
        return "/hh/html/form_basic.html";//质检员表单
    }

    @RequestMapping(path = "/tasks", method = RequestMethod.GET)
    public String getTasks(Model model) {
        List<Task> taskList = taskService.findTaskByQaTypeAndEid("第三方", 1);
        List<Map<String, Object>> taskVoList = new ArrayList<>();
        if (taskList != null) {
            for (Task task : taskList) {
                Map<String, Object> taskVo = new HashMap<>();
                taskVo.put("task", task);
                taskVo.put("startTime", DateUtil.dateToString(task.getStartTime().toString()));
                taskVo.put("reqEnterprise", enterpriseService.getEnterPriseById(requestService.getRequest(1).getReqEid()));
                taskVo.put("inspector1", inspectorService.findInspectorById(task.getInspectorOne()));
                taskVo.put("inspector2", inspectorService.findInspectorById(task.getInspectorTwo()));
                taskVoList.add(taskVo);
            }
        }
        model.addAttribute("tasks", taskVoList);
        if (taskVoList.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }
        return "/hh/html/table_basic2.html";
    }


}
