package com.sayo.qa.CommonUtil;


import com.sayo.qa.entity.Customer;
import org.springframework.stereotype.Component;

/**
 * 持有用户的信息，用于代替session对象，线程隔离
 */
@Component
public class HostHolderCustomer {
    //主要方法有set、get
    private ThreadLocal<Customer> customers = new ThreadLocal<>();
    public void setCustomer(Customer customer){
        customers.set(customer);
    }
    public Customer getCustomer(){
        return customers.get();
    }
    //清除的remove
    public void remove(){
        customers.remove();
    }
}


