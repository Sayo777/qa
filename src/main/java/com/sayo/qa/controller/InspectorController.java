package com.sayo.qa.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.entity.*;
import com.sayo.qa.service.*;
import com.sun.deploy.net.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
    @Autowired
    private ThirdqaService thirdqaService;
    @Autowired
    private SampleService sampleService;


    //质检员列表（政府）
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


    //质检员列表(政府的)
    @RequestMapping(path = "/test",method = RequestMethod.POST)
    @ResponseBody
    public String test(){
        List<Map<String,Object>> list = new ArrayList<>();
        List<String> clothesList = clothesTypeService.getClothesList();
        for(int i = 0;i<clothesList.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("id",i+1);
            map.put("title",clothesList.get(i));
            map.put("subs",getInspectorsByClothesType(i+1,"政府"));
            list.add(map);
        }
        String json = JSON.toJSONString(list);
        return json;
    }

    //质检员列表(第三方质检机构的)
    @RequestMapping(path = "/test1",method = RequestMethod.POST)
    @ResponseBody
    public String test1(int enterpriseId){
        int j = enterpriseId;
        List<Map<String,Object>> list = new ArrayList<>();
        List<String> clothesList = clothesTypeService.getClothesList();
        for(int i = 0;i<clothesList.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("id",i+1);
            map.put("title",clothesList.get(i));
            map.put("subs",get3InspectorsByClothesType(enterpriseId,i+1));
            list.add(map);
        }
        String json = JSON.toJSONString(list);
        return json;
    }


    //第三方质检平台列表
    @RequestMapping(path = "/all3qa",method = RequestMethod.POST)
    @ResponseBody
    public String all3qa(){
        List<Thirdqa> thirdqaList = thirdqaService.findThirdqas();
        List<Map<String,Object>> list1 = new ArrayList<>();
        for (Thirdqa t:thirdqaList
             ) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",t.getId());
            map.put("title",t.getThirdName());
            list1.add(map);
        }

        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("title","所有第三方质检机构");
        map.put("subs",list1);
        list.add(map);
        return JSON.toJSONString(list);
    }

    /**
     * 根据检测服装类型的不同来划分质检员
     * @param clothesType 质检服装类型
     * @param type 质检员工作单位类型
     * @return
     */
    public List<Map<String,Object>> getInspectorsByClothesType(int clothesType,String type){
        List<Inspector> list= inspectorService.findInspectorByTypeAndQaType(type,clothesType);
        List<Map<String,Object>> list1 = new ArrayList<>();
        for (Inspector i: list) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",i.getId());
            map.put("title",i.getName());
            list1.add(map);
        }
        return list1;
    }

    /**
     * 找出某个机构的某类质检员有哪些
     * @param qaType 质检服装类型
     * @param enterpriseId 第三方机构id
     * @return
     */
    public List<Map<String,Object>> get3InspectorsByClothesType(int enterpriseId,int qaType){
        List<Inspector> list= inspectorService.findInspectorByqa3AndQaType(enterpriseId, qaType);
        List<Map<String,Object>> list1 = new ArrayList<>();
        for (Inspector i: list) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",i.getId());
            map.put("title",i.getName());
            list1.add(map);
        }
        return list1;
    }

    //-----------------质检员端客户--------------------------------------------------------------------------------
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getInspectorIndex(){
        return "/hh/inspector/login.html";
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String login(String name, String password,Model model){
        Map<String,Object> map = inspectorService.login(name,password);
        if (map == null || map.size() ==0){
            return "redirect:/inspector/indexInspector";
        }else{
            model.addAttribute("nameMsg",map.get("nameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/hh/inspector/login.html";
        }
    }

    @RequestMapping(path = "/indexInspector",method = RequestMethod.GET)
    public String indexInspector(){
        return "/hh/indexInspector.html";
    }

    /**
     *
     * @param model
     * @param LoginInspector 登录的质检员，从hostholder中获取(暂时用id为2的质检员来代替)
     * @return
     */
    @RequestMapping(path = "/unInspectTask",method = RequestMethod.GET)
    public String getUnInspectTask(Model model,Inspector LoginInspector){
        LoginInspector = inspectorService.findInspectorById(2);
        List<Task> tasks = taskService.findTaskByInsoector0(LoginInspector.getId());
        List<Map<String,Object>> VoList = new ArrayList<>();
        Map<String,Object> map = null;
        for (Task t: tasks) {
            map = new HashMap<>();
            map.put("VoTask",getVoTaskByTask(t)); // 任务编号、受检企业、申请人、联系电话、受检服装类型、受检时间、地址
            map.put("inspector1",inspectorService.findInspectorById(t.getInspectorOne()));
            map.put("inspector2",inspectorService.findInspectorById(t.getInspectorTwo()));
            Sample sample = sampleService.findSampleByTaskId(t.getTaskId());
            if (sample==null){
                map.put("done",false);
                map.put("status",true);//暂无
            }else if(sample.getStatus()==1){
                map.put("done",true);
                map.put("status",true);
            }else{
                map.put("done",false);
                map.put("status",false);
            }

            VoList.add(map);
        }

        model.addAttribute("VoList",VoList);
//        model.addAttribute("done",false);

        return "/hh/inspector/unInspectTask.html";
    }

    //根据taskId 封装出 任务编号、受检企业、申请人、联系电话、受检服装类型
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


    /**
     * 任务执行的身份认证确定
     * @param taskId
     * @param inspector1
     * @param pwd1
     * @param inspector2
     * @param pwd2
     * @return
     */
    @RequestMapping(path = "/confirm",method = RequestMethod.POST)
    @ResponseBody
    public String inspectorConfirm(int taskId,int inspector1,String pwd1,int inspector2,String pwd2){
        Map<String,Object> map = inspectorService.identityConfirm(taskId,inspector1,pwd1,inspector2,pwd2);
        if (map == null || map.size() == 0){
            return CommunityUtil.getJSONString(0,"验证通过");
        }else if (map.get("inspector1Msg")!=null){
            return CommunityUtil.getJSONString(1,map.get("inspector1Msg").toString());
        }else{
            return CommunityUtil.getJSONString(1,map.get("inspector2Msg").toString());
        }
    }

    @RequestMapping(path = "/myWizard",method = RequestMethod.GET)
    public String getMyWizard(){
        return "/hh/inspector/myWizard.html";
    }



    //文件处理上传
    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.file.name}")
    private String headerBucketName;

    @Value("${quniu.bucket.file.url}")
    private String headerBucketUrl;

    @RequestMapping(path = "/myUpload",method = RequestMethod.GET)
    public String getUpload(Model model){
        //生成长传文件的名称
        String fileName = CommunityUtil.generateUUID();
//        String fileName2 = CommunityUtil.generateUUID();
//        String fileName3 = CommunityUtil.generateUUID();
        // 设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));
        // 生成上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);
//        String uploadToken2 = auth.uploadToken(headerBucketName, fileName2, 3600, policy);
//        String uploadToken3 = auth.uploadToken(headerBucketName, fileName3, 3600, policy);

        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);
//        model.addAttribute("uploadToken2", uploadToken2);
//        model.addAttribute("fileName2", fileName2);
//        model.addAttribute("uploadToken3", uploadToken3);
//        model.addAttribute("fileName3", fileName3);
        return "/hh/inspector/webuploader.html";
    }

    /**
     * 根据taskId来记录检测的过程数据
     * @param taskId 任务编码
     * @param model
     * @return
     */
    @RequestMapping(path = "/record/{taskId}",method = RequestMethod.GET)
    public String record(@PathVariable("taskId") String taskId, Model model, HttpServletRequest request){
        //创建session对象
        HttpSession session = request.getSession();
//把用户数据保存在session域对象中
        session.setAttribute("taskId",Integer.parseInt(taskId));

//        System.out.println(taskId);

        String fileName = CommunityUtil.generateUUID();
//        String fileName2 = CommunityUtil.generateUUID();
//        String fileName3 = CommunityUtil.generateUUID();
//        // 设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));
        // 生成上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);
//        String uploadToken2 = auth.uploadToken(headerBucketName, fileName2, 3600, policy);
//        String uploadToken3 = auth.uploadToken(headerBucketName, fileName3, 3600, policy);
        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);
//        model.addAttribute("uploadToken2", uploadToken2);
//        model.addAttribute("fileName2", fileName2);
//        model.addAttribute("uploadToken3", uploadToken3);
//        model.addAttribute("fileName3", fileName3);
        return "/hh/inspector/webuploader.html";
    }

    @RequestMapping(path = "/tosample/{taskId}",method = RequestMethod.GET)
    public String tosample(@PathVariable("taskId") String id, Model model){
        int taskId = Integer.parseInt(id);
        model.addAttribute("taskId",taskId);
        Request r = requestService.getRequest(taskId);
        Customer c = customerService.findCustomerById(r.getReqId());//申请人
        int eId = r.getReqEid();
        Enterprise e = enterpriseService.getEnterPriseById(eId);
        model.addAttribute("enterpriseName",e.getEnterpriseName());
        model.addAttribute("legalPerson",e.getLegalPerson());
        model.addAttribute("contact",c.getName());
        model.addAttribute("tel",r.getContact());
        model.addAttribute("address",e.getProvince()+e.getCity()+e.getCounty());
        model.addAttribute("commercialNumber",e.getCommercialNumber());
        model.addAttribute("organCode",e.getOrganCode());
        model.addAttribute("email",e.getEnterpriseEmail());
        model.addAttribute("website",e.getWebsite());
        model.addAttribute("jingji",e.getType());
        Task task = taskService.findTaskByTaskId(taskId);
        model.addAttribute("insDate",task.getStartTime());
        Thirdqa thirdqa = thirdqaService.findThirdqaById(task.getQaEid());
        model.addAttribute("insName",thirdqa.getThirdName());
        model.addAttribute("insEmail",thirdqa.getThirdEmail());
        model.addAttribute("insContact",thirdqa.getContactPerson());
        model.addAttribute("insTel",thirdqa.getTel());
        model.addAttribute("insAddress",thirdqa.getProvince()+thirdqa.getCity()+thirdqa.getAddress());
        model.addAttribute("ins1",inspectorService.findInspectorById(task.getInspectorOne()).getName());
        model.addAttribute("ins2",inspectorService.findInspectorById(task.getInspectorTwo()).getName());
        return "/hh/inspector/sample.html";
    }

//    @RequestMapping(path = "/sample",method = RequestMethod.GET)
//    public String sample(Model model){
//        return "/hh/inspector/sample.html";
//    }

     @RequestMapping(path = "/process",method = RequestMethod.GET)
    public String process(){
        return "/hh/inspector/process.html";
    }

    @RequestMapping(path = "/findSample",method = RequestMethod.POST)
    @ResponseBody
    public String findSample(int taskId){
        Sample sample = sampleService.findSampleByTaskId(taskId);
        if (sample == null){
            return CommunityUtil.getJSONString(0,"未发送");
        }else{
            return CommunityUtil.getJSONString(1,"正在等待确认中");
        }
    }

    @RequestMapping(path = "/sendSample",method = RequestMethod.POST)
    public String sendSample(int taskId, String level, int jishu, int shuliang,String ways,int bynumber,String byplace,String notes){
        if (notes == null){
            notes = "";
        }
        Sample sample = new Sample();
        sample.setTaskId(taskId);
        sample.setType(level);
        sample.setBasicNumber(jishu);
        sample.setSampleNumber(shuliang);
        sample.setSampleMethod(ways);
        sample.setSampleQuantity(bynumber);
        sample.setPlace(byplace);
        sample.setNotes(notes);
        Task task = taskService.findTaskByTaskId(taskId);
        sample.setSampleDate(task.getStartTime());
        sampleService.addSample(sample);
        return "redirect:/inspector/unInspectTask";
    }


}
