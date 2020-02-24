package com.sayo.qa.controller;

import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.entity.*;
import com.sayo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
