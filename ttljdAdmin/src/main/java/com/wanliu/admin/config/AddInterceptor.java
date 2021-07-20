package com.wanliu.admin.config;

import com.wanliu.admin.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@RestController
public class AddInterceptor implements WebMvcConfigurer {

    @Autowired
    JwtProperties jwtProperties;

    @Override
    @ResponseBody
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new MyInterceptor(jwtProperties))
                .addPathPatterns("/*.do")
                .addPathPatterns("/sys*")
                .addPathPatterns("/*.html")
                .addPathPatterns("/")
                .addPathPatterns("/queryPlayerOne")
                .excludePathPatterns("/extra-login.html")
                .excludePathPatterns("/sysManagerLogin*")
                .excludePathPatterns("/playerlogin.do");
    }
}
