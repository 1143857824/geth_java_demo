package com.wanliu.admin.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class User {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    private String username;

    @JsonIgnore //对象序列化时忽略该字段
    private String password;

    private String gethAddress;

    private String privateKey;

    private BigDecimal money;

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGethAddress() {
        return gethAddress;
    }

    public void setGethAddress(String gethAddress) {
        this.gethAddress = gethAddress;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gethAddress='" + gethAddress + '\'' +
                ", privateKey=" + privateKey +
                ", money=" + money +
                '}';
    }
}