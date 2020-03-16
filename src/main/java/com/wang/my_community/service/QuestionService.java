package com.wang.my_community.service;


import com.wang.my_community.dto.QuestionDto;
import com.wang.my_community.mapper.QuestionMapper;
import com.wang.my_community.mapper.UserMapper;
import com.wang.my_community.model.Question;
import com.wang.my_community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public List<QuestionDto> list() {
       List<Question> questions = questionMapper.list();
       List<QuestionDto> questionDtos = new ArrayList<>();
       for (Question question : questions){
          User user = userMapper.findById(question.getCreator());
           QuestionDto questionDto = new QuestionDto();
           BeanUtils.copyProperties(question,questionDto);
           questionDto.setUser(user);
           questionDtos.add(questionDto);
       }
        return questionDtos;
    }
}
