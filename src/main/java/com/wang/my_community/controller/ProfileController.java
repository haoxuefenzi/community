package com.wang.my_community.controller;

import com.wang.my_community.dto.PaginationDto;
import com.wang.my_community.mapper.QuestionMapper;
import com.wang.my_community.mapper.UserMapper;
import com.wang.my_community.model.User;
import com.wang.my_community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action,
                          @RequestParam(value = "page",defaultValue = "1") Integer page,
                          @RequestParam(value = "size",defaultValue = "3") Integer size,
                          HttpServletRequest request,
                          Model model){

        User user = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user1", user);
                    }
                    break;
                }
            }
        }
        if (user==null){
            return "redirect:/";
        }



        if("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        }else if ("replies".equals(action)){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        PaginationDto paginationDto = questionService.list(user.getId(), page, size);
        model.addAttribute("paginationDto",paginationDto);
        return "profile";
    }

}
