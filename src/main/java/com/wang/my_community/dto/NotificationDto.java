package com.wang.my_community.dto;

import com.wang.my_community.model.User;
import lombok.Data;

@Data
public class NotificationDto {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private String typeName;
    private Long outerId;
    private Integer type;
}
