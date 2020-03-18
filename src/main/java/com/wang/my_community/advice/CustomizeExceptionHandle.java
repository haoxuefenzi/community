package com.wang.my_community.advice;

import com.wang.my_community.excption.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//只能拦截controller异常
@ControllerAdvice
public class CustomizeExceptionHandle {

    @ExceptionHandler(Exception.class)
    ModelAndView Handle(Model model,Throwable e){
//        HttpStatus status = getStatus(request);
        if (e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());
        }else {
            model.addAttribute("message","服务器冒烟啦,要不稍后再试试吧~");
        }
        return new ModelAndView("error");
    }
//    private HttpStatus getStatus(HttpServletRequest request){
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode==null){
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return HttpStatus.valueOf(statusCode);
//    }
}
