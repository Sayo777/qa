package com.sayo.qa;

import com.sayo.qa.CommonUtil.DateUtil;
import com.sayo.qa.dao.ManagerMapper;
import com.sayo.qa.entity.Admin;
import com.sayo.qa.entity.Manager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QaApplicationTests {
    @Autowired
    ManagerMapper managerMapper;

    @Test
    void contextLoads() {
        Manager manager = new Manager(1,"芈月","123","女","1566266387","2828@163.com", DateUtil.stringToDate("1998-09-27"),
                1,"第三方");
        managerMapper.insertManager(manager);
        System.out.println(manager);
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
