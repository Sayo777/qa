package com.sayo.qa.controller;

import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.entity.*;
import com.sayo.qa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private RequestService requestService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ClothesTypeService clothesTypeService;
    @Autowired
    private ReqArrangeService reqArrangeService;
    @Autowired
    private TaskService taskService;

    //没有被审核的质检任务请求
    @RequestMapping(path = "/uncheckedReq",method = RequestMethod.GET)
    public String getCheckedTask(Model model){
        List<Request>  uncheckList = requestService.findReqByStatus("未审核");
        List<Map<String,Object>>  VoList = new ArrayList<>();
        for (Request r: uncheckList) {
            Map<String,Object> map = new HashMap<>();
            map.put("req",r);
            Enterprise e = enterpriseService.getEnterPriseById(r.getReqEid());
            map.put("reqE",e);
            Customer c = customerService.findCustomerById(r.getReqId());
            map.put("reqC",c);
            map.put("reqType",clothesTypeService.getClothesType(r.getReqType()));
            VoList.add(map);
        }
        model.addAttribute("uncheckList",VoList);
        return "/hh/table_uncheckTask";
    }

    //审核企业的质检申请
    /*这里应该是要进行事务处理的*/
    @RequestMapping(path = "/checkStatus",method = RequestMethod.POST)
    @ResponseBody
    public String checkStatus(int reqId,String status,String reason){
        //将审核结果添加到审核任务表中
        TaskCheck taskCheck = new TaskCheck();
        taskCheck.setReqId(reqId);
        taskCheck.setResult(status);
        taskCheck.setReason(reason);
        /*这里是系统当前登录的人id，注意是政府的manager*/
        taskCheck.setCheckerId(3);
        taskCheck.setCheckTime(new Date());
        taskService.insertTaskCheck(taskCheck);
        requestService.updateReqStatus(reqId,status);
        if (status.equals("通过")){
            //将“通过”任务添加到任务待安排表中req_arrange
            ReqArrange reqArrange = new ReqArrange();
            reqArrange.setRequestId(reqId);
            reqArrange.setIsArrange(0);
            reqArrangeService.insertArrange(reqArrange);
        }
        return CommunityUtil.getJSONString(0,"审核成功！");
    }

    //待处理任务列表
    @RequestMapping(path = "/unfinishTask",method = RequestMethod.GET)
    public String getUnfinishTask(Model model){
        //这里是没有被安排的质检任务
        List<ReqArrange> reqArranges = taskService.findReqByIsArrange(0);
        List<Map<String,Object>> list = new ArrayList<>();
        for (ReqArrange r:reqArranges) {
            Map<String,Object> map = new HashMap<>();
            //任务编号req.requestId  申请企业enterpriseName 申请人reqName 申请质检类型reqType 申请时间reqTime 审核时间taskCheck.checkTime
            map.put("req",r);
            Request request = requestService.getRequest(r.getRequestId());
            Enterprise e = enterpriseService.getEnterPriseById(request.getReqEid());
            map.put("enterpriseName",e.getEnterpriseName());
            map.put("reqName",customerService.findCustomerById(request.getReqId()).getName());
            map.put("reqType",clothesTypeService.getClothesType(request.getReqType()));
            map.put("reqTime",request.getReqTime());
            TaskCheck taskCheck = taskService.findTaskCheckByReqId(r.getRequestId());
            map.put("taskCheck",taskCheck.getCheckTime());
            list.add(map);
        }
        model.addAttribute("list",list);
        return "/hh/unfinishArrange.html";
    }
}
