package com.wang.my_community.controller;

import com.wang.my_community.dto.PaginationDto;
import com.wang.my_community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String Index(Model model,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "3") Integer size) {

        PaginationDto paginationDto = questionService.list(page,size);
        model.addAttribute("paginationDto", paginationDto);
        return "index";
    }
}
