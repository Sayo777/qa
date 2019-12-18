package com.sayo.qa;

import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.dao.CustomerMapper;
import com.sayo.qa.dao.InspectorMapper;
import com.sayo.qa.dao.ManagerMapper;
import com.sayo.qa.entity.Admin;
import com.sayo.qa.entity.Customer;
import com.sayo.qa.entity.Inspector;
import com.sayo.qa.entity.Manager;
import com.sayo.qa.service.ThirdUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class QaApplicationTests {
    @Autowired
    ManagerMapper managerMapper;
    @Autowired
    ThirdUserService thirdUserService;
    @Autowired
    InspectorMapper inspectorMapper;
    @Autowired
    CustomerMapper customerMapper;

    @Test
    public void test(){
        List<Inspector> inspectors = inspectorMapper.selectInspector();
        System.out.println(inspectors);
    }

    @Test
    public  void contextLoads() {
        Customer customer = new Customer();
        customer.setName("企业用户2");
        customer.setPassword("123");
        customer.setEmail("123@33.com");
        customerMapper.insertSelective(customer);

        System.out.println(customerMapper.selectCustomerByName("企业用户2"));
    }
    public int MoreThanHalfNum_Solution(int [] array) {
        int times = array.length/2;
        int number =0;
        for(int i=0;i<array.length;i++){
            int a=array[i];
            for(int j = i+1;j<array.length;j++){
                 number = 0;
                if(array[j] == a){
                    number++;
                }
            }
            if(number>times){
                return a;
            }
        }
        return 0;
    }

}
