package com.sayo.qa.controller;

import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.entity.*;
import com.sayo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inspector")
public class InspectorController {
    @Autowired
    private InspectorService inspectorService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private  ClothesTypeService clothesTypeService;

    //质检员列表
    @RequestMapping(path = "/searchInspectorGov",method = RequestMethod.GET)
    public String getInspectorGov(Model model){
        List<Inspector> inspectors = inspectorService.findInspectorsByType("政府");
        model.addAttribute("inspectors",inspectors);
        return "/hh/table_inspectorGov.html";
    }

    //质检员详细信息
    @RequestMapping(path = "/detail/{Id}",method = RequestMethod.GET)
    public String InspectorDetail(@PathVariable("Id") int id, Model model){
        Inspector i= inspectorService.findInspectorById(id);
        model.addAttribute("iDetail",i);
        return "/hh/inspectorDetail_Gov.html";
    }

    //质检员的任务安排表
    @RequestMapping(path = "/TaskArrange",method = RequestMethod.GET)
    public String getTasks(Model model){
        List<Task> taskList = taskService.findTaskByQaTypeAndEid("政府",1);
        List<Map<String,Object>> taskVoList = new ArrayList<>();
        if (taskList!=null){
            for (Task task:taskList) {
                Map<String,Object> taskVo = new HashMap<>();
                taskVo.put("task",task);
                taskVo.put("startTime", DateUtil.dateToString(task.getStartTime().toString()));
                taskVo.put("reqEnterprise",enterpriseService.getEnterPriseById(requestService.getRequest(1).getReqEid()));
                taskVo.put("inspector1",inspectorService.findInspectorById(task.getInspectorOne()));
                taskVo.put("inspector2",inspectorService.findInspectorById(task.getInspectorTwo()));
                taskVoList.add(taskVo);
            }
        }
        model.addAttribute("tasks",taskVoList);
        return "/hh/arrangeTasks_Gov.html";
    }

    //质检员任务详情
    @RequestMapping(path = "/TaskArrange/{taskId}",method = RequestMethod.GET)
    public String TaskArrangeDetail(@PathVariable("taskId") int taskId, Model model){
        Task task = taskService.findTaskByTaskId(taskId);
        Request request = requestService.getRequest(taskId);
        Customer reqCustomer = customerService.findCustomerById(request.getReqId());
        model.addAttribute("request",request);
        model.addAttribute("reqCustomer",reqCustomer);
        model.addAttribute("clothesType",clothesTypeService.getClothesType(request.getReqType()));
        model.addAttribute("task",task);
        model.addAttribute("startTime", task.getStartTime());
        model.addAttribute("reqEnterprise",enterpriseService.getEnterPriseById(requestService.getRequest(taskId).getReqEid()));
        model.addAttribute("inspector1",inspectorService.findInspectorById(task.getInspectorOne()));
        model.addAttribute("inspector2",inspectorService.findInspectorById(task.getInspectorTwo()));
        return "/hh/taskArrangeDetail_Gov.html";
    }





}
