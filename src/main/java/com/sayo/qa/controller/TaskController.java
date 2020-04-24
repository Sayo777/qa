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

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private ResultService resultService;

    @Autowired
    private ManagerService managerService;
    @Autowired
    private MessageService messageService;

    //待审核的质检任务请求
    @RequestMapping(path = "/uncheckedReq",method = RequestMethod.GET)
    public String getCheckedTask(Model model,Page page){
        List<Request>  uncheckList = requestService.findReqByStatus("未审核",page.getOffset(),page.getLimit());
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
        page.setRows(requestService.findReqByStatusRows("未审核"));
        page.setPath("/task/uncheckedReq");
        if (VoList.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }else{

            model.addAttribute("uncheckList",VoList);
        }
        return "/hh/table_uncheckTask";
    }

    //审核企业的质检申请
    /*这里应该是要进行事务处理的*/
    @RequestMapping(path = "/checkStatus",method = RequestMethod.POST)
    @ResponseBody
    public String checkStatus(int reqId, String status, String reason, HttpServletRequest request){
        //将审核结果添加到审核任务表中
        Request r = requestService.getRequest(reqId);
        Customer customer = customerService.findCustomerById(r.getReqId());
        TaskCheck taskCheck = new TaskCheck();
        taskCheck.setReqId(reqId);
        taskCheck.setResult(status);
        taskCheck.setReason(reason);
        Manager loginGovManager = managerService.findManagerByName((String)request.getSession().getAttribute("loginGovManager"));
        taskCheck.setCheckerId(loginGovManager.getId());
        taskCheck.setCheckTime(new Date());
        taskService.insertTaskCheck(taskCheck);
        requestService.updateReqStatus(reqId,status);
        if (status.equals("通过")){
            //将“通过”任务添加到任务待安排表中req_arrange
            ReqArrange reqArrange = new ReqArrange();
            reqArrange.setRequestId(reqId);
            reqArrange.setIsArrange(0);
            reqArrange.setGaozhiConfirm(0);//告知书未确认
            reqArrangeService.insertArrange(reqArrange);
            Message message = new Message();
            message.setToId(customer.getId());
            message.setType("告知单确认");
            messageService.addMessage(message);
        }
        return CommunityUtil.getJSONString(0,"审核成功！");
    }

    //待处理任务列表
    @RequestMapping(path = "/unfinishTask",method = RequestMethod.GET)
    public String getUnfinishTask(Model model,Page page){
        //这里是没有被安排的质检任务
        List<ReqArrange> reqArranges = taskService.findReqByIsArrange(0,page.getOffset(),page.getLimit());
        page.setRows(taskService.findReqByIsArrangeRows(0));
        page.setPath("/task/unfinishTask");
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
        List<Inspector> inspectors = inspectorService.findInspectorsByType("第三方",0,1000);
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
        if (reqArranges.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }
        return "/hh/unfinishArrange.html";
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
        ThirdTask thirdTask = new ThirdTask();
        thirdTask.setTaskId(reqId);
        thirdTask.setQaEid(thirdqaService.findIdByName(qaName));
        thirdTaskService.addThirdTask(thirdTask);
        //将请求安排表req_arrange表该请求的isArrange变为1
        reqArrangeService.updateReqArrange(reqId);
        return CommunityUtil.getJSONString(0,"安排给"+qaName+"成功！");
    }


    @Autowired
    private ThirdTaskService thirdTaskService;
    /**
     * 安排给第三方并且指定质检员
     * @param reqId 请求编码
     * @param arrangeTime 安排时间
     * @param qaName 第三方质检机构名称
     * @param inspector1 质检员1
     * @param inspector2 质检员2
     * @return
     */
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
        //给ThirdTask添加记录
        ThirdTask thirdTask = new ThirdTask();
        thirdTask.setTaskId(reqId);
        thirdTask.setQaEid(thirdqaService.findIdByName(qaName));
        thirdTask.setIns1(inspectorService.findInspectorByName(inspector1).getId());
        thirdTask.setIns2(inspectorService.findInspectorByName(inspector1).getId());
        thirdTaskService.addThirdTask(thirdTask);
        //将请求安排表req_arrange表该请求的isArrange变为1
        reqArrangeService.updateReqArrange(reqId);
        return CommunityUtil.getJSONString(0,"安排给"+qaName+"成功！"+inspector1+"---"+inspector2);
    }


    @RequestMapping(path = "/test",method = RequestMethod.POST)
    @ResponseBody
    public Object test11(String enterpriseName){
        int qa3Id = thirdqaService.findIdByName(enterpriseName);
        List<Inspector> list = inspectorService.findInspectorByqa3(qa3Id);
        return list;
    }

    /**
     * 已完成的质检任务
     * @param model
     * @return
     */
    @RequestMapping(path = "/finishedTask",method = RequestMethod.GET)
    public String FinishedTask(Model model,Page page){
        List<Result> results = resultService.findResults(page.getOffset(),page.getLimit());
        page.setRows(resultService.findResultsRows());
        page.setPath("/task/finishedTask");
        List<Map<String,Object>> Volist = new ArrayList<>();
        Map<String,Object> map = null;
        for (Result r: results) {
            map = new HashMap<>();
            //申请信息
            Request request = requestService.getRequest(r.getTaskId());
            map.put("reqEName",enterpriseService.getEnterPriseById(request.getReqEid()).getEnterpriseName()); //申请企业
            map.put("reqName",customerService.findCustomerById(request.getReqId()).getName());//申请人
            map.put("reqType",clothesTypeService.getClothesType(request.getReqType()));//申请质检类型
            Task task = taskService.findTaskByTaskId(r.getTaskId());
            map.put("task",task); //任务编号(task.taskId)、质检时间(task.startTime)
            //质检报告(r.resultId),质检流程详情(r.qaProcess)
            map.put("result",r);
            Volist.add(map);
        }
        model.addAttribute("Volist",Volist);
        if (Volist.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }
        return "/hh/table_finishedTask.html";
    }

}
