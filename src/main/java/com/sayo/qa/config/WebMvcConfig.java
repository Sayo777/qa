package com.sayo.qa.config;

import com.sayo.qa.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//之前创建配置类主要是为了生成一个第三方的bean
//而这里不一样,实现一个接口
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(loginTicketInterceptor).excludePathPatterns("/**/*.css",
                "/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");

    }
}
