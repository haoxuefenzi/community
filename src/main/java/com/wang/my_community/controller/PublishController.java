package com.wang.my_community.controller;

import com.wang.my_community.dto.QuestionDto;
import com.wang.my_community.model.User;
import com.wang.my_community.mapper.QuestionMapper;
import com.wang.my_community.model.Question;
import com.wang.my_community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Integer id,
                       Model model){
        QuestionDto questionDto = questionService.getById(id);

        model.addAttribute("title",questionDto.getTitle());
        model.addAttribute("tag",questionDto.getTag());
        model.addAttribute("description",questionDto.getDescription());
        model.addAttribute("id",questionDto.getId());

        return "publish";
    }

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(//???????????????????????????????????????????????
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Integer id,
            HttpServletRequest request, Model model){

        model.addAttribute("title",title);
        model.addAttribute("tag",tag);
        model.addAttribute("description",description);
        if(title==null||(title=="")){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description==null||(description=="")){
            model.addAttribute("error","问题描述不能为空");
            return "publish";
        }
        if(tag==null||(tag=="")){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            model.addAttribute("error","你还未登录，请登录");
            return "publish";
        }
        Question question = new Question();

        question.setTitle(title);
        question.setTag(tag);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setId(id);
        if(question.getViewCount()==null){
            question.setViewCount(0);
        }
        if(question.getLikeCount()==null){
            question.setLikeCount(0);
        }
        if(question.getCommentCount()==null){
            question.setCommentCount(0);
        }

        questionService.createOrUpdate(question);

        return "redirect:/";
    }
}
