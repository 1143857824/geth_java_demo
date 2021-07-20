package com.wanliu.admin.service;

import com.wanliu.admin.entity.ReturnResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public interface UserService {

    /**
     * 注册
     * @param username
     * @param password
     * @return
     * @throws IOException
     */
    ReturnResult register(String username, String password) throws IOException;

    /**
     * 登录
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    ReturnResult login(String username, String password, HttpServletRequest request, HttpServletResponse response);

    /**
     * 查询账单
     * @param aoData
     * @param request
     * @return
     */
    String findBillAll(String aoData, HttpServletRequest request) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    /**
     * 查询账户余额
     * @param request
     * @return
     */
    ReturnResult findMoneyById(HttpServletRequest request);

    /**
     * 转账
     * @param otherUsername
     * @param outMoney
     * @param request
     * @return
     */
    ReturnResult transfer(String otherUsername, BigDecimal outMoney, HttpServletRequest request) throws Exception;
}
