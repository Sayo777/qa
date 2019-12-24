package com.sayo.qa.controller;

import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.dao.TaskMapper;
import com.sayo.qa.entity.Task;
import com.sayo.qa.entity.ThirdUser;
import com.sayo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/3User")
public class ThirdUserController {
    @Autowired
    private ThirdUserService thirdUserService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private RequestService requestService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private InspectorService inspectorService;

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String login3User(String name, String password, String role, Model model){
        return "";
    }

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String index(){
       return "/hh/login.html";
    }

    @RequestMapping(path = "/inspector/detail",method = RequestMethod.GET)
    public String getDetailinspector(){
        return "/hh/html/form_basic.html";//质检员表单
    }

    @RequestMapping(path = "/tasks",method = RequestMethod.GET)
    public String getTasks(Model model){
        List<Task> taskList = taskService.findTaskByQaTypeAndEid("第三方",1);
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
        return "/hh/html/table_basic2.html";
    }


}
