package com.wanliu.admin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author 梁涛
 * 编写时间 2021_6_1
 * 修改时间 2021_6_1
 * 输出日志
 * 自定义日志输出","号隔开，只有0时不输出日志，有1时输出传入参数，有2时输出逻辑处理中的日志，有3时输出返回值日志，含有100输出所有
 * serverName:这个是为了区分日志输出的系统是哪个，一般用于一个tomcat部署多个项目
 */
@Configuration
@EnableConfigurationProperties(Sout.class)
@ConfigurationProperties(prefix = "mylog")
public class Sout {

    //自定义日志输出","号隔开，只有0时不输出日志，有1时输出传入参数，有2时输出逻辑处理中的日志，有3时输出返回值日志，含有100输出所有
    private String flog = "100";
    private String serverName = "";

    private List<String> list = new ArrayList<>();

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getFlog() {
        return flog;
    }

    public void setFlog(String flog) {
        this.flog = flog;
    }

    @Autowired
    private Sout sout;
    private static Sout soutStatic;

    /**
     * 执行前执行
     */
    @PostConstruct
    public void init() {
        soutStatic = this.sout;
        String[] split = soutStatic.flog.split(",");
        soutStatic.list = Arrays.asList(split);
    }

    //全部输出
    public static void spl(String str, Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(!(soutStatic.list.size() == 1 && soutStatic.list.get(0).equals("0"))) {
            System.out.println(soutStatic.serverName + "------> " + format + "  **    " + str + "    **  |  " + o);
        }
    }
    //全部输出
    public static void sPl(Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(!(soutStatic.list.size() == 1 && soutStatic.list.get(0).equals("0"))) {
            System.out.println(soutStatic.serverName + "------> " + format + "  **     无     **  |  " + o);
        }
    }
    //全部输出不换行
    public static void sP(Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(!(soutStatic.list.size() == 1 && soutStatic.list.get(0).equals("0"))) {
            System.out.println(soutStatic.serverName + "------> " + format + "  **     无     **  |  " + o);
        }
    }
    //全部输出不换行
    public static void sP(String str, Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(!(soutStatic.list.size() == 1 && soutStatic.list.get(0).equals("0"))) {
            System.out.println(soutStatic.serverName + "------> " + format + "  **     " + str + "     **  |  " + o);
        }
    }
    //输出参数时使用
    public static void splParam(String str, Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(soutStatic.list.contains("1") || soutStatic.list.contains("100")) {
            System.out.println(soutStatic.serverName + "---p---> " + format + "  **     " + str + "     **  |  " + o);
        }
    }
    //输出参数时使用
    public static void splParam(Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(soutStatic.list.contains("1") || soutStatic.list.contains("100")) {
            System.out.println(soutStatic.serverName + "---p---> " + format + "  **     无     **  |  " + o);
        }
    }
    //编写处理过程时使用
    public static void splService(String str, Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(soutStatic.list.contains("2") || soutStatic.list.contains("100")) {
            System.out.println(soutStatic.serverName + "---s---> " + format + "  **     " + str + "     **  |  " + o);
        }
    }
    //编写处理过程时使用
    public static void splService(Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(soutStatic.list.contains("2") || soutStatic.list.contains("100")) {
            System.out.println(soutStatic.serverName + "---s---> " + format + "  **     无     **  |  " + o);
        }
    }
    //输出返回值时使用
    public static void splReturn(String str, Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(soutStatic.list.contains("3") || soutStatic.list.contains("100")) {
            System.out.println(soutStatic.serverName + "---r---> " + format + "  **     " + str + "     **  |  " + o);
        }
    }
    //输出返回值时使用
    public static void splReturn(Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(soutStatic.list.contains("3") || soutStatic.list.contains("100")) {
            System.out.println(soutStatic.serverName + "---r---> " + format + "  **     无     **  |  " + o);
        }
    }

    //输出返回值时使用
    public static void splOther(String str, Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(soutStatic.list.contains("4") || soutStatic.list.contains("100")) {
            System.out.println(soutStatic.serverName + "---o---> " + format + "  **     " + str + "     **  |  " + o);
        }
    }
    //输出返回值时使用
    public static void splOther(Object o){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = sdf.format(new Date());
        if(soutStatic.list.contains("4") || soutStatic.list.contains("100")) {
            System.out.println(soutStatic.serverName + "---o---> " + format + "  **     无     **  |  " + o);
        }
    }
}
