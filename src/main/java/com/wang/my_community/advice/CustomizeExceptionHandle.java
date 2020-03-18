package com.wang.my_community.advice;

import com.alibaba.fastjson.JSON;
import com.wang.my_community.dto.ResultDto;
import com.wang.my_community.excption.CustomizeErrorCode;
import com.wang.my_community.excption.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//只能拦截controller异常
@ControllerAdvice
public class CustomizeExceptionHandle {

    @ExceptionHandler(Exception.class)
    ModelAndView Handle(Model model, Throwable e,
                        HttpServletRequest request,
                        HttpServletResponse response){
        String contentType = request.getContentType();
        //contentType不能放前面，有可能会空指针异常
        if ("application/json".equals(contentType)){
            ResultDto resultDto;
            if (e instanceof CustomizeException) {
                resultDto = ResultDto.errorOf((CustomizeException) e);
            } else {
                resultDto = ResultDto.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            try {
                response.setCharacterEncoding("utf-8");
                response.setStatus(200);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDto));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        } else {
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
