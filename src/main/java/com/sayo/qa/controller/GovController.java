package com.sayo.qa.controller;

import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.entity.*;
import com.sayo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private ThirdqaService thirdqaService;
    @Autowired
    private InformationService informationService;

    //跳转至新消息界面
    @RequestMapping(path = "/news",method = RequestMethod.GET)
    public String toNews(Model model,Page page){
        page.setPath("/gov/news");
        page.setRows(informationService.findRowsInformationsBytoid(0));
        List<Information> informationList = informationService.findInformationsBytoid(0,page.getOffset(),page.getLimit());
        List<Map<String,Object>> list = new ArrayList<>();

        for (Information information:informationList){
            Map<String,Object> map = new HashMap<>();
            Enterprise enterprise = enterpriseService.getEnterPriseById(customerService.findCustomerById(information.getfromid()).getId());
            map.put("enterprise",enterprise);
            map.put("information",information);
            if (information.getContent().equals("质检申请")){
                map.put("ahref","/task/uncheckedReq");
            }
            list.add(map);
        }
        model.addAttribute("list",list);

        //将消息变成已读
        informationService.updateStatusBytoId(0);

        return "/hh/gov/news.html";
    }


    //跳转添加质检员页面
    @RequestMapping(path = "/addInspector",method = RequestMethod.GET)
    public String toaddinspector(Model model){
        return "/hh/gov/addInspector.html";
    }
    //添加质检员
    @RequestMapping(path = "/addInspector",method = RequestMethod.POST)
    public String addinspector(Model model,String name,int inspectType){
        Inspector inspector = new Inspector();
        inspector.setType("政府");
        inspector.setName(name);
        inspector.setEnterpriseId(1);
        inspector.setInspectType(inspectType);
        inspectorService.addInsSelective(inspector);
        model.addAttribute("msg","添加成功！");
        return "/hh/gov/addInspector.html";
    }

    @RequestMapping(path = "/checkThirdqa", method = RequestMethod.POST)
    @ResponseBody
    public String checkThirdqa(int id, String status) {
        int i = thirdqaService.updateNotesById(id, status);
        if (i < 0) {
            return CommunityUtil.getJSONString(-1, "审核出错了！");
        } else {
            return CommunityUtil.getJSONString(0, "审核成功");
        }
    }

    @RequestMapping(path = "/inf/{id}", method = RequestMethod.GET)
    public String toInf(@PathVariable("id") int id, Model model) {
        Thirdqa thirdqa = thirdqaService.findThirdqaById(id);
        model.addAttribute("t", thirdqa);
        return "/hh/gov/thirdqaInf.html";
    }

    @RequestMapping(path = "/thirdqaCheck", method = RequestMethod.GET)
    public String tothirdqaCheck(Model model, Page page) {
        page.setPath("/gov/thirdqaCheck");
        page.setRows(thirdqaService.findRowsThirdqasByNotes("未通过"));
        List<Thirdqa> thirdqaList = thirdqaService.findThirdqasByNotes("未通过", page.getOffset(), page.getLimit());
        model.addAttribute("t", thirdqaList);
        if (thirdqaList.size() == 0) {
            model.addAttribute("hint", "暂时没有记录！");
        }
        return "/hh/gov/checkThirdqa.html";
    }

    @RequestMapping(path = "/findThirdqa", method = RequestMethod.GET)
    public String tofindThirdqa(Model model, Page page) {
        page.setPath("/gov/findThirdqa");
        page.setRows(thirdqaService.findRowsThirdqasByNotes("通过"));
        List<Thirdqa> thirdqaList = thirdqaService.findThirdqasByNotes("通过", page.getOffset(), page.getLimit());
        model.addAttribute("t", thirdqaList);
        if (thirdqaList.size() == 0) {
            model.addAttribute("hint", "暂时没有记录！");
        }
        return "/hh/gov/thirdqas.html";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String toLogin() {
        return "/hh/gov/loginGov.html";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String loginGov(String name, String password, String role, Model model, HttpServletRequest request) {
        Map<String, Object> map = managerService.loginGov(name, password, role, "政府");
        if (map.isEmpty() || map == null) {
            if ("管理员".equals(role)) {//跳转第三方管理员
                request.getSession().setAttribute("loginGovManager", name);
                return "redirect:/gov/index";
            } else {
                return "/hh/indexInspector.html";
            }
        } else {
            model.addAttribute("nameMsg", map.get("nameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/hh/gov/loginGov.html";
        }
    }

    @RequestMapping(path = "/insYes", method = RequestMethod.POST)
    @ResponseBody
    public String insYes(int taskId) {
        //更新task表、thirdTask表
        ThirdTask thirdTask = thirdTaskService.findThirdTaskById(taskId);
        thirdTaskService.updateStatusByTaskIdAndCode(3, taskId);
        Task task = new Task();
        task.setTaskId(taskId);
        task.setInspectorOne(thirdTask.getIns1());
        task.setInspectorTwo(thirdTask.getIns2());

        int i = taskService.updateTaskSelective(task);
        if (i < 0) {
            return CommunityUtil.getJSONString(1, "审核出错啦！");
        } else {
            return CommunityUtil.getJSONString(0, "审核成功！");
        }
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndex(Model model) {
        int status0 = informationService.findRowsInformationsBytoid0(0);
        if (status0 != 0){
            model.addAttribute("status0",status0);
        }
        return "/hh/indexGov";
    }


    @RequestMapping(value = "/checkIns", method = RequestMethod.GET)
    public String toCheckIns(Model model) {
        List<Task> task = taskService.findTaskforCheckIns();
        List<Map<String, Object>> VoList = new ArrayList<>();
        Map<String, Object> map = null;
        for (Task t : task) {
            map = new HashMap<>();
            map.put("VoTask", getVoTaskByTask(t)); // 任务编号、受检企业、申请人、联系电话、受检服装类型、受检时间、地址
            ThirdTask thirdTask = thirdTaskService.findThirdTaskById(t.getTaskId());
            map.put("inspector1", inspectorService.findInspectorById(thirdTask.getIns1()));
            map.put("inspector2", inspectorService.findInspectorById(thirdTask.getIns2()));
            VoList.add(map);
        }
        if (VoList.size() == 0) {
            model.addAttribute("hint", "暂时没有记录！");
        } else {
            model.addAttribute("VoList", VoList);
        }

        return "/hh/gov/check_ins.html";
    }

    //已完成任务列表
    @RequestMapping(path = "/finishedTask", method = RequestMethod.GET)
    public String finishedTask(HttpServletRequest request, Model model, Page page) {
        //用request.getSession()来获取当前登录的人员，先暂时用质检员id=2的来展示
//        String inspectorName = (String)request.getSession().getAttribute("inspectorName");
//        Inspector inspector = inspectorService.findInspectorByName(inspectorName);
        List<Task> taskList = taskService.findFinishTaskAll(page.getOffset(), page.getLimit());
        page.setRows(taskService.findFinishTaskAllRows());
        page.setPath("/gov/finishedTask");
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
        model.addAttribute("tasks", taskVoList);
        if (taskVoList.size() == 0) {
            model.addAttribute("hint", "暂时没有记录！");
        }
        return "/hh/gov/finishedTask.html";
    }

    //已拒绝的任务
    @RequestMapping(path = "/refuseTask", method = RequestMethod.GET)
    public String refuseTask(HttpServletRequest request, Model model, Page page) {
        List<Request> refuseTask = requestService.findRefuseTask(page.getOffset(), page.getLimit());
        page.setRows(requestService.findRefuseTaskRows());
        page.setPath("/gov/refuseTask");
        List<Map<String, Object>> refuseList = new ArrayList<>();
        if (refuseTask != null) {
            for (Request r : refuseTask) {
                Map<String, Object> map = new HashMap<>();
                TaskCheck taskCheck = taskCheckService.findTaskCheckByReqId(r.getId());
                Customer customer = customerService.findCustomerById(r.getReqId());
                String clothesType = clothesTypeService.getClothesType(r.getReqType());
                Product product = productService.findProductById(r.getProductId());
                map.put("r", r);
                map.put("taskcheck", taskCheck);
                map.put("customer", customer.getName());
                map.put("clothesType", clothesType);
                map.put("product", product.getProductName());
                refuseList.add(map);
            }
        }
        model.addAttribute("refuseList", refuseList);
        if (refuseList.size() == 0) {
            model.addAttribute("hint", "暂时没有记录！");
        }
        return "/hh/gov/refuseTask.html";
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
}
