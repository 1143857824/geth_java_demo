package com.wanliu.admin.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jnr.ffi.annotations.In;
import org.web3j.abi.datatypes.Int;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class Bill {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String Username;

    private String otherUsername;

    private String address;

    private Integer outOrIn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getOutOrIn() {
        return outOrIn;
    }

    public void setOutOrIn(Integer outOrIn) {
        this.outOrIn = outOrIn;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", Username='" + Username + '\'' +
                ", otherUsername='" + otherUsername + '\'' +
                ", address='" + address + '\'' +
                ", outOrIn=" + outOrIn +
                ", createTime=" + createTime +
                '}';
    }
}