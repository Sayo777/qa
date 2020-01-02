package com.sayo.qa.controller;

import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.CommonUtil.DateUtil;
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
    @Autowired
    private InspectorService inspectorService;
    @Autowired
    private ThirdqaService thirdqaService;

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

        //质检机构
        List<Thirdqa> thirdqaList = thirdqaService.findThirdqas();
        model.addAttribute("thirdQa",thirdqaList);
        List<Inspector> inspectors = inspectorService.findInspectorsByType("第三方");
        List<Map<String,Object>> VoList = new ArrayList<>();
        List<String> qaTypeList = clothesTypeService.getClothesList();
        for (String type:qaTypeList) {
            Map<String,Object> map = new HashMap<>();
            List<Inspector> list1 = inspectorService.findInspectorByTypeAndQaType("第三方",clothesTypeService.findIdByTypeName(type));

            map.put("type",type);
            map.put("list1",list1);
            VoList.add(map);
        }
        model.addAttribute("Volist",VoList);

        model.addAttribute("inspectorsList",inspectors);
        return "/hh/unfinishArrange.html";
    }

    @RequestMapping(path = "/hi",method = RequestMethod.GET)
    public String test(){
        return "/hh/html/form_wizard.html";
    }

    /**
     * 安排政府质检任务,安排完后更新任务安排表req_arrange
     * @param reqId 任务请求编码
     * @param arrangeTime 安排时间
     * @param inspector1 质检员1
     * @param inspector2 质检员2
     * @return
     */
    @RequestMapping(path = "/taskToGov",method = RequestMethod.POST)
    @ResponseBody
    public String taskToGov(int reqId,String arrangeTime,String inspector1,String inspector2){
        Task task = new Task();
        task.setTaskId(reqId);
        task.setQaType("政府");
        task.setQaEid(1);
        task.setInspectorOne(inspectorService.findInspectorByName(inspector1).getId());
        task.setInspectorTwo(inspectorService.findInspectorByName(inspector2).getId());
        task.setStartTime(DateUtil.stringToDate(arrangeTime));
        taskService.addTaskSelective(task);
        //将请求安排表req_arrange表该请求的isArrange变为1
        reqArrangeService.updateReqArrange(reqId);
        return CommunityUtil.getJSONString(0,"安排成功！");
    }
    //安排第三方质检任务(没有指定人)
    @RequestMapping(path = "/taskTo3un",method = RequestMethod.POST)
    @ResponseBody
    public String taskToGov(int reqId,String arrangeTime,String qaName){
        Task task = new Task();
        task.setTaskId(reqId);
        task.setQaType("第三方");
        task.setQaEid(thirdqaService.findIdByName(qaName));
        task.setStartTime(DateUtil.stringToDate(arrangeTime));
        taskService.addTaskSelective(task);
        return CommunityUtil.getJSONString(0,"安排给"+qaName+"成功！");
    }


    @RequestMapping(path = "/taskTo3",method = RequestMethod.POST)
    @ResponseBody
    public String taskToGov(int reqId,String arrangeTime,String qaName,String inspector1,String inspector2){
        Task task = new Task();
        task.setTaskId(reqId);
        task.setQaType("第三方");
        task.setQaEid(thirdqaService.findIdByName(qaName));
        task.setInspectorOne(inspectorService.findInspectorByName(inspector1).getId());
        task.setInspectorTwo(inspectorService.findInspectorByName(inspector2).getId());
        task.setStartTime(DateUtil.stringToDate(arrangeTime));
        taskService.addTaskSelective(task);
        return CommunityUtil.getJSONString(0,"安排给"+qaName+"成功！"+inspector1+"---"+inspector2);
    }


    @RequestMapping(path = "/test",method = RequestMethod.POST)
    @ResponseBody
    public Object test11(String enterpriseName){
        int qa3Id = thirdqaService.findIdByName(enterpriseName);
        List<Inspector> list = inspectorService.findInspectorByqa3(qa3Id);
        return list;
    }
}
