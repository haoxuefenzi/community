package com.wang.my_community.excption;

//????????????????????????????????????????????????????????
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(4001,"你找的问题不见啦,要不换个试试吧~"),
    TARGET_PARAM_NOT_FOUND(4002,"未选择任何问题或评论"),
    NO_LOGIN(4003,"当前操作需要登录,请登录"),
    SYSTEM_ERROR(4004,"服务器冒烟啦,要不稍后再试试吧~"),
    TYPE_PARAM_WRONG(4005,"评论类型错误或不存在"),
     COMMENT_NOT_FOUND(4006,"评论不见啦,要不换个试试吧~");
    private String message;
    private Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
