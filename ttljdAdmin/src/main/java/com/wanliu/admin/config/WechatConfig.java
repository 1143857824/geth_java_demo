package com.wanliu.admin.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 梁涛
 * 微信小程序读取配置文件类
 */
@Configuration
@EnableConfigurationProperties(WechatConfig.class)
@ConfigurationProperties(prefix = "qiuxingka.wechat")
public class WechatConfig {

    String appid;

    String secret;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "WechatConfig{" +
                "appid='" + appid + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
