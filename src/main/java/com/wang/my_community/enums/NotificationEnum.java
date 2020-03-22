package com.wang.my_community.enums;

public enum NotificationEnum {
    READ(1),
    UNREAD(0);
    private int status;

    public int getStatus() {
        return status;
    }

    NotificationEnum(int status) {
        this.status = status;
    }


}
