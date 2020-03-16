package com.wang.my_community.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.wang.my_community.dto.QuestionDto;
import com.wang.my_community.mapper.QuestionMapper;
import com.wang.my_community.mapper.UserMapper;
import com.wang.my_community.model.User;
import com.wang.my_community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String Index(HttpServletRequest request, Model model){

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if (user != null) {
                    request.getSession().setAttribute("user1",user);
                }
                break;
            }
        }
        List<QuestionDto> questionDtos = questionService.list();
        model.addAttribute("questionDtos",questionDtos);
        return "index";
    }
}
