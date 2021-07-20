package com.wanliu.admin.config.timedTask;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
public class everydayReset {

    //3.添加定时任务
    //每天1点执行
    @Scheduled(cron = "0 0 0 */1 * ?")
    //或直接指定时间间隔，例如：5秒
//    @Scheduled(fixedRate=5000)
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        System.out.println("日任务清零完成");
    }
}
