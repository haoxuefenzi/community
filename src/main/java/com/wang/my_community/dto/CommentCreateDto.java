package com.wang.my_community.dto;

import lombok.Data;

@Data
public class CommentCreateDto {
    private Long parentId;
    private String content;
    private Integer type;
    private Long id;
    private int msg;
}
