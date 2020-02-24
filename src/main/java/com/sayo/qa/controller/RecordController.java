package com.sayo.qa.controller;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.sayo.qa.CommonUtil.CommunityUtil;
import com.sayo.qa.entity.*;
import com.sayo.qa.jFree.NewTest;
import com.sayo.qa.jFree.WordUtil;
import com.sayo.qa.service.*;
import org.apache.commons.lang3.StringUtils;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ThirdqaService thirdqaService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FileUtil fileUtil;

    @Autowired
    ResultService resultService;

    /**
     * 完成质检
     * @param key 质检报告的filename
     * @param request
     * @return
     */
    @RequestMapping(path = "/finish",method = RequestMethod.POST)
    public String finish(String key,HttpServletRequest request){
        String resultSrc = headerBucketUrl + "/" + key;
        Result result = new Result();
        int taskId = (int)request.getSession().getAttribute("taskId");
        result.setTaskId(taskId);
        result.setResultSrc(resultSrc);
        resultService.addSelectiveResult(result);
        //完成质检后将task表的endTime更新上去，表示当前任务已经完成
        Task task = new Task();
        task.setTaskId(taskId);
        task.setEndTime(new Date());
        taskService.updateTaskSelective(task);
        return "redirect:/inspector/unInspectTask";
    }


    @RequestMapping(path = "/download", method = RequestMethod.POST)
    public void download(HttpServletRequest request, HttpServletResponse response) {
        //获取信息
        HttpSession session = request.getSession();
        String path = (String) session.getAttribute("wordPath");
        try {
            NewTest wd = new NewTest();
            wd.download(response, path);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.file.name}")
    private String headerBucketName;

    @Value("${quniu.bucket.file.url}")
    private String headerBucketUrl;

    @RequestMapping(path = "/add1", method = RequestMethod.POST)
    public String add(int outlook, int size, int materia, int craft, int wash, int ztang, int pack, String standard,
                      int keyMateria, int qualityGuarantee, int hasReport, int isIllegal,
                      HttpServletRequest request,String key,String key2,String key3,String key4,String key5,String key6, Model model) {
        HttpSession session = request.getSession();
//        int taskId = (int) session.getAttribute("taskId");
        int taskId = 10;

        String urlheader = "http://q5zmtmsy3.bkt.clouddn.com/";
        String notification_url = urlheader +key;
        String undertaking_url = urlheader +key2;
        String inspectorsPhoto_url = urlheader +key3;
        String environment1_url = urlheader +key4;
        String environment2_url = urlheader +key5;
        String license_url = urlheader +key6;
        Record r = new Record();
        r.setTaskId(taskId);
        r.setNotification(notification_url);
        r.setUndertaking(undertaking_url);
        r.setInspectorsphoto(inspectorsPhoto_url);
        r.setEnvironment1(environment1_url);
        r.setEnvironment2(environment2_url);
        r.setLicense(license_url);
        r.setOutlook(outlook);
        r.setSize(size);
        r.setMateria(materia);
        r.setCraft(craft);
        r.setWash(wash);
        r.setZtang(ztang);
        r.setPack(pack);
        r.setStandard(standard);
        r.setKeymateria(keyMateria);
        r.setQualityguarantee(qualityGuarantee);
        r.setHasreport(hasReport);
        r.setIsillegal(isIllegal);
        int i = recordService.addRecord(r);

        Request request1 = requestService.getRequest(taskId);
        Product product = productService.findProductById(request1.getProductId());
        model.addAttribute("p",product);
        Customer customer = customerService.findCustomerById(request1.getReqId());
        Enterprise enterprise = enterpriseService.getEnterPriseById(request1.getReqEid());
        enterprise.setAddress(enterprise.getProvince() + enterprise.getCity() + enterprise.getCounty() + enterprise.getAddress());

        Map<String, Object> map = new HashMap<>();
        map.put("outlook", changehege(outlook));
        map.put("size", changehege(size));
        map.put("materia", changehege(materia));
        map.put("craft", changehege(craft));
        map.put("wash", changehege(wash));
        map.put("ztang", changehege(ztang));
        map.put("pack", changehege(pack));
        map.put("standard", standard);
        map.put("keyMateria", change(keyMateria));
        map.put("qualityGuarantee", change(qualityGuarantee));
        map.put("hasReport", change(hasReport));
        map.put("isIllegal", change(isIllegal));
        //承检机构
        Task task = taskService.findTaskByTaskId(taskId);
        Thirdqa thirdqa = thirdqaService.findThirdqaById(task.getQaEid());
        map.put("qaName",thirdqa.getThirdName());
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        map.put("qaTime",dateFormat.format(new Date()));
        map.put("eName",enterprise.getEnterpriseName());
        map.put("comerciaNumber",enterprise.getCommercialNumber());
        map.put("address",enterprise.getAddress());
        map.put("legalperson",enterprise.getLegalPerson());
        map.put("contactor",customer.getName());
        map.put("phone","15940035052");
        map.put("pName","newb短袖");
        map.put("pDoc","雪纺条纹 蝙蝠衫");




        String randFilename = UUID.randomUUID().toString().replaceAll("-", "") + ".docx";
        String dir = "D:/work/wordTemplate";
        session.setAttribute("wordPath", dir + "/" + randFilename);
        WordUtil.exportWord("D:/work/wordTemplate/resultTemplate.docx", dir, randFilename, map);

        if (i < 0) {
            return CommunityUtil.getJSONString(-1, "哎呀！出错啦！");
        } else {

            model.addAttribute("e", enterprise);
            model.addAttribute("customer", customer.getName());

            model.addAttribute("outlook",changehege(outlook));
            model.addAttribute("size",changehege(size));
            model.addAttribute("materia",changehege(materia));
            model.addAttribute("craft",changehege(craft));
            model.addAttribute("wash",changehege(wash));
            model.addAttribute("ztang",changehege(ztang));
            model.addAttribute("pack",changehege(pack));
            model.addAttribute("keyMateria",change(keyMateria));
            model.addAttribute("qualityGuarantee",change(qualityGuarantee));
            model.addAttribute("hasReport",change(hasReport));
            model.addAttribute("isIllegal",change(isIllegal));
            model.addAttribute("standard",standard);



            // 上传文件名称
            String fileName = CommunityUtil.generateUUID();
            // 设置响应信息
            StringMap policy = new StringMap();
            policy.put("returnBody", CommunityUtil.getJSONString(0));
            // 生成上传凭证
            Auth auth = Auth.create(accessKey, secretKey);
            String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);

            model.addAttribute("uploadToken", uploadToken);
            model.addAttribute("fileName", fileName);







            return "/hh/records/result_quality.html";
        }
    }


    @Autowired
    private ProductService productService;

    /**
     * 生成的《质量情况表》展示
     *
     * @return
     */
    @RequestMapping(path = "/recordForm", method = RequestMethod.GET)
    public String downloadInfo(Model model, HttpServletRequest req) {
//        int id = (int) req.getSession().getAttribute("taskId"); // taskId
        int id = 10;
        Request request = requestService.getRequest(id);
        Product product = productService.findProductById(request.getProductId());
        model.addAttribute("p",product);
        Customer customer = customerService.findCustomerById(request.getReqId());
        Enterprise enterprise = enterpriseService.getEnterPriseById(request.getReqEid());
        enterprise.setAddress(enterprise.getProvince() + enterprise.getCity() + enterprise.getCounty() + enterprise.getAddress());
        model.addAttribute("e", enterprise);
        model.addAttribute("customer", customer.getName());
        Record r = recordService.findRecordByTaskId(id);
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
        return "/hh/records/result_quality.html";
    }

    /**
     * 根据返回值来判断是否合格
     * @param i 1-合格 0-不合格
     * @return
     */
    public String changehege(int i){
        if(i == 0){
            return "不合格";
        }else{
            return "合格";
        }
    }

    /**
     *
     * @param i 1-是 0-否
     * @return
     */
    public String change(int i){
        if(i == 1){
            return "是";
        }else{
            return "否";
        }
    }
}
