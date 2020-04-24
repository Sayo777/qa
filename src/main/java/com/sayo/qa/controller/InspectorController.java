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
import java.lang.invoke.VolatileCallSite;
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
    public String getInspectorGov(Model model,Page page){
        page.setPath("/inspector/searchInspectorGov");
        page.setRows(inspectorService.findRowsInspectorsByType("政府"));
        List<Inspector> inspectors = inspectorService.findInspectorsByType("政府",page.getOffset(),page.getLimit());
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (Inspector i : inspectors){
            Map<String,Object> map = new HashMap<>();
            String type = clothesTypeService.getClothesType(i.getInspectType());
            map.put("i",i);
            map.put("type",type);
            mapList.add(map);
        }
        model.addAttribute("inspectors",mapList);
        if (mapList.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }
        return "/hh/inspector/table_inspectorGov.html";
    }



    //查找所有的质检员数量
    @RequestMapping(path = "/findInsCount",method = RequestMethod.POST)
    public String findInsCount(){
        int count = inspectorService.findInspectorMaxId();
        return CommunityUtil.getJSONString(count);
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
        if (taskVoList.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }else{

            model.addAttribute("tasks",taskVoList);
        }
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
    public String login(String name, String password,Model model,HttpServletRequest request){
        Map<String,Object> map = inspectorService.login(name,password);
        if (map == null || map.size() ==0){
            request.getSession().setAttribute("inspectorName",name);
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
     * @param model 登录的质检员，从hostholder中获取(暂时用id为2的质检员来代替)
     * @return
     */
    @RequestMapping(path = "/unInspectTask",method = RequestMethod.GET)
    public String getUnInspectTask(Model model,HttpServletRequest request,Page page){
        Inspector  LoginInspector = inspectorService.findInspectorByName((String) request.getSession().getAttribute("inspectorName"));
        if (LoginInspector == null){
            return "redirect:/inspector/login";
        }
        List<Task> tasks = taskService.findTaskByInsoector0(LoginInspector.getId(),page.getOffset(),page.getLimit());
        page.setPath("/inspector/unInspectTask");
        page.setRows(taskService.findTaskByInsoector0Rows(LoginInspector.getId()));
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
        if (VoList.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }
        model.addAttribute("VoList",VoList);
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




    @Value("sayo-process1")
    private String headerBucketName;
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
        session.setAttribute("taskId",Integer.parseInt(taskId));
        String fileName1 = CommunityUtil.generateUUID();
        String fileName2 = CommunityUtil.generateUUID();
        String fileName3 = CommunityUtil.generateUUID();
        String fileName4 = CommunityUtil.generateUUID();
        String fileName5 = CommunityUtil.generateUUID();
        String fileName6 = CommunityUtil.generateUUID();
        // 设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));
        // 生成上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken1 = auth.uploadToken(headerBucketName, fileName1, 3600, policy);
        String uploadToken2 = auth.uploadToken(headerBucketName, fileName2, 3600, policy);
        String uploadToken3 = auth.uploadToken(headerBucketName, fileName3, 3600, policy);
        String uploadToken4 = auth.uploadToken(headerBucketName, fileName4, 3600, policy);
        String uploadToken5 = auth.uploadToken(headerBucketName, fileName5, 3600, policy);
        String uploadToken6 = auth.uploadToken(headerBucketName, fileName6, 3600, policy);
        model.addAttribute("uploadToken1", uploadToken1);
        model.addAttribute("fileName1", fileName1);
        model.addAttribute("uploadToken2", uploadToken2);
        model.addAttribute("fileName2", fileName2);
        model.addAttribute("uploadToken3", uploadToken3);
        model.addAttribute("fileName3", fileName3);
        model.addAttribute("uploadToken4", uploadToken4);
        model.addAttribute("fileName4", fileName4);
        model.addAttribute("uploadToken5", uploadToken5);
        model.addAttribute("fileName5", fileName5);
        model.addAttribute("uploadToken6", uploadToken6);
        model.addAttribute("fileName6", fileName6);
        return "/hh/inspector/webuploader.html";
    }

    @Autowired
    private ProductService productService;
    @RequestMapping(path = "/tosample/{taskId}",method = RequestMethod.GET)
    public String tosample(@PathVariable("taskId") String id, Model model){
        int taskId = Integer.parseInt(id);
        model.addAttribute("taskId",taskId);
        Request r = requestService.getRequest(taskId);
        Product product = productService.findProductById(r.getProductId());
        model.addAttribute("p",product);
        model.addAttribute("date",new Date());
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



    //已完成的任务列表
    @RequestMapping(path = "/finishedTask",method = RequestMethod.GET)
    public String finishedTask(HttpServletRequest request,Model model){
        //用request.getSession()来获取当前登录的人员，先暂时用质检员id=2的来展示
        String inspectorName = (String)request.getSession().getAttribute("inspectorName");
        Inspector inspector = inspectorService.findInspectorByName(inspectorName);
        List<Task> taskList = taskService.findFinishedTaskByInspector(inspector.getId());
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
        if (taskVoList.size()==0){
            model.addAttribute("hint","暂时没有记录！");
        }
        model.addAttribute("tasks",taskVoList);
        return "/hh/inspector/finishedTask.html";
    }


    @Autowired
    private RecordService recordService;
    @Autowired
    private ResultService resultService;

    /**
     * 已完成的任务详情
     * @param taskId 任务编码
     * @param model
     * @return
     */
    @RequestMapping(path = "/finishedDetail/{taskId}",method = RequestMethod.GET)
    public String finishedDetail(@PathVariable("taskId") int taskId, Model model){

//        int id = (int) req.getSession().getAttribute("taskId"); // taskId
        Request request = requestService.getRequest(taskId);
        Customer customer = customerService.findCustomerById(request.getReqId());
        Enterprise enterprise = enterpriseService.getEnterPriseById(request.getReqEid());
        Task task = taskService.findTaskByTaskId(taskId);
        Inspector inspector1 = inspectorService.findInspectorById(task.getInspectorOne());
        Inspector inspector2 = inspectorService.findInspectorById(task.getInspectorTwo());
        enterprise.setAddress(enterprise.getProvince() + enterprise.getCity() + enterprise.getCounty() + enterprise.getAddress());
        model.addAttribute("e", enterprise);
        model.addAttribute("customer", customer);
        model.addAttribute("ins1",inspector1.getName());
        model.addAttribute("ins2",inspector2.getName());
        model.addAttribute("insDate",task.getEndTime());
        Record r = recordService.findRecordByTaskId(taskId);
        model.addAttribute("r",r);
        model.addAttribute("outlook",changehege(r.getOutlook()));
        model.addAttribute("size",changehege(r.getSize()));
        model.addAttribute("materia",changehege(r.getMateria()));
        model.addAttribute("craft",changehege(r.getCraft()));
        model.addAttribute("wash",changehege(r.getWash()));
        model.addAttribute("ztang",changehege(r.getZtang()));
        model.addAttribute("pack",changehege(r.getPack()));
        model.addAttribute("keyMateria",change(r.getKeymateria()));
        model.addAttribute("qualityGuarantee",change(r.getQualityguarantee()));
        model.addAttribute("hasReport",change(r.getHasreport()));
        model.addAttribute("isIllegal",change(r.getIsillegal()));
        model.addAttribute("standard",r.getStandard());
        Result result = resultService.findResultByTaskId(taskId);
        model.addAttribute("resultSrc",result.getResultSrc());

        return "/hh/inspector/taskDetail.html";
    }
    public String changehege(int i){
        if(i == 0){
            return "不合格";
        }else{
            return "合格";
        }
    }
    public String change(int i){
        if(i == 1){
            return "是";
        }else{
            return "否";
        }
    }

}
