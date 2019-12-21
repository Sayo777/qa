package com.sayo.qa.service;

import com.sayo.qa.dao.*;
import com.sayo.qa.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private EnterpriseMapper enterpriseMapper;
    @Autowired
    private EApplyMapper eApplyMapper;
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private ClothesTypeMapper clothesTypeMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private InspectorMapper inspectorMapper;

    public Map<String, Object> registerCustomer(String name, String password, String email) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(name)) {
            map.put("nameMsg", "名字不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(email)) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }

            //查看Customer表
            Customer customer =customerMapper.selectCustomerByName(name);
            if (customer != null) {
                System.out.println("该账号已经注册"+customer.getName());
                map.put("nameMsg", "该账号已存在!");
                return map;
            }
            // 验证邮箱
            customer = customerMapper.selectCustomerByEmail(email);
            if (customer != null) {
                map.put("emailMsg", "该邮箱已被注册!");
                return map;
            }
            Customer c = new Customer();
            c.setName(name);
            c.setPassword(password);
            c.setEmail(email);
            //注册企业用户
            customerMapper.insertSelective(c);
        return map;
    }

    public Map<String,Object> loginCustomer(String name,String password){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(name)){
            map.put("nameMsg","姓名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        //判断该User存在与否

            Customer customer = customerMapper.selectCustomerByName(name);
            if (customer == null){
                map.put("nameMsg","该用户不存在");
                return map;
            }
            if (!password.equals(customer.getPassword())){
                map.put("passwordMsg","密码不正确");
                return map;
            }
        return map;
    }

    //企业信息认证申请
    public Map<String,Object> apply(Enterprise enterprise){
        //申请企业认证
        Map<String,Object> map = new HashMap<>();
        Enterprise e = enterpriseMapper.selectByEname(enterprise.getEnterpriseName());
        if (e!=null && e.getIsValid() == 1){ //已经认证通过了
            map.put("hasApplied","该企业已经认证过了");
            return map;
        }
        if (e == null){ //企业信息表添加企业信息
            enterpriseMapper.insertSelective(enterprise);
        }
//        if (e.getIsValid() == 0){ //认证未通过的
//            map.put("existsEnterprise","该企业已经认证过了");
//            return map;
//        }
        //第一次申请认证的

        EApply eApply = new EApply();
        /**
         * 申请人的编码，这里要修改成登录人的编码！！！！！！！！！！！！！
         */
        eApply.setApplierId(1);
        eApply.setApplyEname(enterprise.getEnterpriseName());
        eApply.setApplyTime(new Date());
        eApplyMapper.insertSelective(eApply);
        return map;
    }

    //获取已经认证过的企业信息（通过和不通过的一起展示在列表里）
    //customerId是当前登录的用户
    public List<Map<String,Object>> getApplied(int customerId){
        List<Map<String,Object>> eApplyVoList = new ArrayList<>();
        int eid = customerMapper.selectEIdByCustomerId(customerId);
        String eName = enterpriseMapper.selectByPrimaryKey(eid).getEnterpriseName();
        List<EApply> eApplyList = eApplyMapper.selectByEName(eName);
        for (EApply eA: eApplyList) {
            Map<String,Object> map = new HashMap<>();
            //申请编号、申请时间、审核时间、审核结果、
            map.put("eApply",eA);
            String result = "通过";
            if (eA.getCheckResult() == 0){
                result = "未通过";
            }
            if (eA.getCheckResult() == -1){
                result = "未审核";
            }
            map.put("checkResult",result);
            //申请人
            map.put("applier",customerMapper.selectByPrimaryKey(eA.getApplierId()).getName());
            eApplyVoList.add(map);
        }
     return eApplyVoList;
    }

    //质检申请
    public Map<String,Object> qaReq(String eName,int reqType,String reqName,String contact){
        Map<String,Object> map = new HashMap<>();
        Customer c = customerMapper.selectCustomerByName(reqName);
        if (c==null){
            map.put("reqName","没有该用户");
            return map;
        }
        Enterprise e = enterpriseMapper.selectByEname(eName);
        if (e==null || e.getIsValid() == 0){
            map.put("eName","该企业未通过认证");
            return map;
        }
        Request r = new Request();
        r.setReqId(c.getId());
        r.setReqEid(e.getId());
        r.setContact(contact);
        r.setReqType(reqType);
        r.setReqTime(new Date());
        requestMapper.insertSelective(r);
        return map;
    }

    //申请记录表
    public List<Map<String,Object>> getRequest(int customerId){
        List<Request> list = requestMapper.selectByReqId(customerId);
        List<Map<String,Object>> reqList = new ArrayList<>();
        for (Request r: list) {
            Map<String, Object> map = new HashMap<>();
            map.put("request",r); //申请任务编号、申请时间、申请情况、详情理由
            //申请人、企业名称、质检服装类型
            Customer c = customerMapper.selectByPrimaryKey(r.getReqId());
            map.put("reqName",c.getName());
            ClothesType clothesType = clothesTypeMapper.selectByPrimaryKey(r.getReqType());
            map.put("clothType",clothesType.getTypeName());
            reqList.add(map);
        }
        return reqList;
    }

    //质检结果记录表
    public List<Map<String,Object>> getReqrecord(int customerId){
        List<Map<String,Object>> reqRecordList = new ArrayList<>();
        Customer c = customerMapper.selectByPrimaryKey(customerId);
        //找出该用户所在公司的质检申请（通过的记录）
        List<Request> requestList = requestMapper.selectByReqEId0(c.getEnterpriseId());
        for (Request r:requestList) {
            Map<String,Object> map = new HashMap<>();
            Task t = taskMapper.selectByPrimaryKey(r.getId());
            //任务编号、执行机构类型、完成时间
            map.put("task",t);
            //执行单位名称???????
            map.put("qaEName","质检机构xxxx");
            // 质检服装类型、质检员1，质检员2
            map.put("reqClothesType",clothesTypeMapper.selectByPrimaryKey(r.getReqType()).getTypeName());
            Inspector i1 = inspectorMapper.selectByPrimaryKey(t.getInspectorOne());
            Inspector i2 = inspectorMapper.selectByPrimaryKey(t.getInspectorTwo());
            map.put("inspector1",i1.getName());
            map.put("inspector2",i2.getName());
            reqRecordList.add(map);
        }
        return reqRecordList;
    }
}
