package com.sayo.qa.service;

import com.sayo.qa.dao.InspectorMapper;
import com.sayo.qa.dao.TaskMapper;
import com.sayo.qa.entity.Inspector;
import com.sayo.qa.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InspectorService {
    @Autowired
    InspectorMapper inspectorMapper;
    @Autowired
    TaskMapper taskMapper;

    public List<Inspector> findInspectors(){
        return inspectorMapper.selectInspector();
    }

    public Inspector findInspectorById(int id){
        return inspectorMapper.selectByPrimaryKey(id);
    }

    public List<Inspector> findInspectorsByType(String type){
        return inspectorMapper.selectInspectorByType(type);
    }

    public List<Inspector> findInspectorByTypeAndQaType(String type,int qaType){
        return inspectorMapper.selectInspectorByTypeAndQaType(type,qaType);
    }

    public List<Inspector> findInspectorByqa3AndQaType(int enterpriseId,int qaType){
        return inspectorMapper.selectInspectorByqa3AndQaType(enterpriseId,qaType);
    }
    public List<Inspector> findInspectorByqa3(int enterpriseId){
        return inspectorMapper.selectInspectorByqa3(enterpriseId);
    }
    public Inspector findInspectorByName(String name){
        return inspectorMapper.selectInspectorByname(name);
    }

    /**
     * 判断当前任务是否是指定的质检员
     * @param taskId 任务编号
     * @param inspector1 1号质检员账号
     * @param pwd1 1号质检员密码
     * @param inspector2 2号质检员账号
     * @param pwd2 2号质检员密码
     * @return
     */
    public Map<String,Object> identityConfirm(int taskId,int inspector1,String pwd1,int inspector2,String pwd2){
        Task task = taskMapper.selectTaskByTaskId(taskId);
        Inspector trueInspector1 = inspectorMapper.selectByPrimaryKey(task.getInspectorOne());
        Inspector trueInspector2 = inspectorMapper.selectByPrimaryKey(task.getInspectorTwo());
        Map<String,Object> map = new HashMap<>();
        if (inspector1 != trueInspector1.getId() || (!pwd1.equals(trueInspector1.getPassword()))){
            map.put("inspector1Msg","1号质检员验证失败");
            return map;
        }
        if (inspector2 != trueInspector2.getId() || (!pwd2.equals(trueInspector2.getPassword()))){
            map.put("inspector2Msg","2号质检员验证失败");
            return map;
        }
        return map;
    }

    public Map<String, Object> login(String name, String password) {
        Map<String, Object> map = new HashMap<>();
        //判断该User存在与否
        Inspector inspector = inspectorMapper.selectInspectorByname(name);
        if (inspector == null) {
            map.put("nameMsg", "该用户不存在");
            return map;
        }
        if (!password.equals(inspector.getPassword())) {
            map.put("passwordMsg", "密码不正确");
            return map;
        }
        return map;
    }
}
