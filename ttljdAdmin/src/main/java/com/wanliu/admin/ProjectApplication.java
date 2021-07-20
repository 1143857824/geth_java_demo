package com.wanliu.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import tk.mybatis.spring.annotation.MapperScan;

import java.io.IOException;

/**
 * @author:梁涛
 * springboot启动类
 */
@SpringBootApplication
@MapperScan("com.wanliu.admin.mapper")
public class ProjectApplication extends SpringBootServletInitializer {

    /**
     * 需要把web项目打成war包部署到外部tomcat运行时需要改变启动方式
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(ProjectApplication.class);
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ProjectApplication.class,args);
    }
}
