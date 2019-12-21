package com.sayo.qa.interceptor;

import com.sayo.qa.CommonUtil.CookieUtil;
import com.sayo.qa.CommonUtil.HostHolderCustomer;
import com.sayo.qa.entity.Customer;
import com.sayo.qa.entity.LoginTicket;
import com.sayo.qa.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private CustomerService customerService;
    //获取ticket来查找User

    @Autowired
    private HostHolderCustomer hostHolderCustomer;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request,"ticket");
        if (ticket!=null){
            //查询凭证，还要查看凭证是不是有效，如果无效，那就相当于没登录

            LoginTicket loginTicket = customerService.findLoginTicket(ticket);
            if (loginTicket!=null && loginTicket.getStatus()==0 && loginTicket.getExpired().after(new Date())){
                //根据凭证查找user
                Customer customer = customerService.findCustomerById(loginTicket.getUserId());
                //在本次请求当中是持有用户，保存下来.在多线程之间隔离对象ThreadLocal
                hostHolderCustomer.setCustomer(customer);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //先从hostholder里得到user
        Customer customer = hostHolderCustomer.getCustomer();
        if (customer!=null && modelAndView !=null){
            modelAndView.addObject("loginCustomer",customer);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolderCustomer.remove();
    }
}
