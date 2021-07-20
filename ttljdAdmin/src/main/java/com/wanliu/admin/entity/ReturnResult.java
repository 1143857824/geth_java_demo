package com.wanliu.admin.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 梁涛
 * 返回数据的一个封装类
 */
public class ReturnResult<T> implements Serializable {
    /**
     * 正确or错误状态码
     */
    String status;
    /**
     * 错误消息
     */
    String message;

    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date());
    /**
     * 返回值
     */
    T model;

    public ReturnResult() {
    }

    public ReturnResult(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ReturnResult(String status, String message, T model) {
        this.status = status;
        this.message = message;
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ReturnResult{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", model=" + model +
                '}';
    }
}