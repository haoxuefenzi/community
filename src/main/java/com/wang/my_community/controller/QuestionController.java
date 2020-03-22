package com.wang.my_community.controller;

import com.wang.my_community.dto.CommentDto;
import com.wang.my_community.dto.QuestionDto;
import com.wang.my_community.enums.CommentTypeEnum;
import com.wang.my_community.service.CommentService;
import com.wang.my_community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model){

        QuestionDto questionDto = questionService.getById(id);
        List<QuestionDto> relatedQuestions = questionService.selectRelated(questionDto);

        List<CommentDto> commentDtos = commentService.
                listByTargetId(id, CommentTypeEnum.QUESTION);

        //阅读数
        questionService.incView(id);

        model.addAttribute("question",questionDto);
        model.addAttribute("commentDtos",commentDtos);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
