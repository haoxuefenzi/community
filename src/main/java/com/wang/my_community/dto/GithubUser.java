package com.wang.my_community.dto;

public class GithubUser {
    private String login;
    private int id;
    private String node_id;


    public GithubUser() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public GithubUser(String login, int id, String node_id) {
        this.login = login;
        this.id = id;
        this.node_id = node_id;
    }
}
