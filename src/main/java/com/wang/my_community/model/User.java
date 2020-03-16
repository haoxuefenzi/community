package com.wang.my_community.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private Integer id;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String nodeId;
    private String avatarUrl;
}
