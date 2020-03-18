package com.wang.my_community.dto;


import com.wang.my_community.excption.CustomizeErrorCode;
import com.wang.my_community.excption.CustomizeException;
import lombok.Data;


@Data
public class ResultDto {
    private Integer code;
    private String message;

    public static ResultDto errorOf(Integer code, String message) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }
    public static ResultDto errorOf(CustomizeErrorCode errorCode){
        return errorOf(errorCode.getCode(),errorCode.getMessage());
    }
    public static ResultDto errorOf(CustomizeException e){
        return errorOf(e.getCode(),e.getMessage());
    }
    public static ResultDto okOf(){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(4000);
        resultDto.setMessage("请求成功");
        return resultDto;
    }
}
