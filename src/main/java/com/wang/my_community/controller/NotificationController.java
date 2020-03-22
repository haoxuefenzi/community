package com.wang.my_community.controller;

import com.wang.my_community.dto.NotificationDto;
import com.wang.my_community.dto.PaginationDto;
import com.wang.my_community.enums.NotificationTypeEnum;
import com.wang.my_community.model.User;
import com.wang.my_community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable("id") Long id, HttpServletRequest request, Model model){

        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return "";
        }
        NotificationDto notificationDto = notificationService.read(id,user);
        if (NotificationTypeEnum.REPLY_COMMENT.getType()==notificationDto.getType()
        ||NotificationTypeEnum.REPLY_QUESTION.getType()==notificationDto.getType()){
            return "redirect:/question/"+notificationDto.getOuterId();
        }else {
            return "redirect:/";
        }
    }
}
