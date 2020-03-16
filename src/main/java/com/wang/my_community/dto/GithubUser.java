package com.wang.my_community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubUser {
    private String login;
    private int id;
    private String nodeId;
    private String avatarUrl;

}
