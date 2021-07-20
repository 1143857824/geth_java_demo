package com.wanliu.admin.controller;

import com.alibaba.fastjson.JSON;
import com.wanliu.admin.entity.ReturnResult;
import com.wanliu.admin.enu.ReturnResultStatus;
import com.wanliu.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询账单
     * @param aoData
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("findBillAll")
    public String findBillAll(
            @RequestParam String aoData,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try {
            return userService.findBillAll(aoData, request);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new ReturnResult(ReturnResultStatus.ERROR, ReturnResultStatus.ERROR_MESSAGE));
        }
    }

    /**
     * 查询金额
     * @param request
     * @param response
     * @return
     */
    @PostMapping("findMoneyById")
    public ResponseEntity<ReturnResult> findMoneyById(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try {
            return ResponseEntity.ok(userService.findMoneyById(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ReturnResult(ReturnResultStatus.ERROR, ReturnResultStatus.ERROR_MESSAGE));
        }
    }

    /**
     * 转账
     * @param request
     * @param response
     * @return
     */
    @PostMapping("transfer")
    public ResponseEntity<ReturnResult> transfer(
            String otherUsername,
            BigDecimal outMoney,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        try {
            /**
             * 非空判断
             */
            if (otherUsername == null || "".equals(otherUsername.trim()) || outMoney == null) {
                return ResponseEntity.ok(new ReturnResult(ReturnResultStatus.ISNULL, "账号、金额不能为空"));
            }
            return ResponseEntity.ok(userService.transfer(otherUsername, outMoney, request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ReturnResult(ReturnResultStatus.ERROR, ReturnResultStatus.ERROR_MESSAGE));
        }
    }

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<ReturnResult> register(
            String username,
            String password
    ) {
        try {
            /**
             * 非空判断
             */
            if (username == null || "".equals(username.trim()) || password == null || "".equals(password.trim())) {
                return ResponseEntity.ok(new ReturnResult(ReturnResultStatus.ISNULL, ReturnResultStatus.ISNULL_MESSAGE));
            }
            return ResponseEntity.ok(userService.register(username, password));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ReturnResult(ReturnResultStatus.ERROR, ReturnResultStatus.ERROR_MESSAGE));
        }
    }

    /**
     * 登录
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<ReturnResult> login(
            String username,
            String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            /**
             * 非空判断
             */
            if (username == null || "".equals(username.trim()) || password == null || "".equals(password.trim())) {
                return ResponseEntity.ok(new ReturnResult(ReturnResultStatus.ISNULL, ReturnResultStatus.ISNULL_MESSAGE));
            }
            return ResponseEntity.ok(userService.login(username, password, request, response));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ReturnResult(ReturnResultStatus.ERROR, ReturnResultStatus.ERROR_MESSAGE));
        }
    }
}
