package com.wang.my_community.model;

public class User {
    private String name;
    private Integer id;
    private String accountId;
    private String token;
    private long gmtCreate;
    private long gmtModified;

    public User() {
    }

    public User(String name, Integer id, String accountId, String token, long gmtCreate, long gmtModified) {
        this.name = name;
        this.id = id;
        this.accountId = accountId;
        this.token = token;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }
}
