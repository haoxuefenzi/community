package com.wang.my_community.controller;

import com.wang.my_community.dto.PaginationDto;
import com.wang.my_community.dto.QuestionDto;
import com.wang.my_community.mapper.UserMapper;
import com.wang.my_community.model.User;
import com.wang.my_community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String Index(HttpServletRequest request, Model model,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "3") Integer size) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if (user != null) {
                    request.getSession().setAttribute("user1", user);
                }
                break;
            }
        }
        PaginationDto paginationDto = questionService.list(page,size);
        model.addAttribute("paginationDto", paginationDto);
        return "index";
    }
}
