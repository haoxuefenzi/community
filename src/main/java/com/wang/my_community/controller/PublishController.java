package com.wang.my_community.controller;

import com.wang.my_community.model.User;
import com.wang.my_community.mapper.QuestionMapper;
import com.wang.my_community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(//???????????????????????????????????????????????
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
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
        User user = (User) request.getSession().getAttribute("user1");
        if(user==null){
            model.addAttribute("error","你还未登录，请登录");
            return "publish";
        }
        Question question = new Question();

        question.setTitle(title);
        question.setTag(tag);
        question.setDescription(description);
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        question.setCreator(user.getId());


        if(question.getViewCount()==null){
            question.setViewCount(0);
        }
        if(question.getLikeCount()==null){
            question.setLikeCount(0);
        }
        if(question.getCommentCount()==null){
            question.setCommentCount(0);
        }

        questionMapper.create(question);

        return "redirect:/";
    }
}
