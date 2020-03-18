package com.wang.my_community.service;


import com.wang.my_community.dto.PaginationDto;
import com.wang.my_community.dto.QuestionDto;
import com.wang.my_community.mapper.QuestionMapper;
import com.wang.my_community.mapper.UserMapper;
import com.wang.my_community.model.Question;
import com.wang.my_community.model.QuestionExample;
import com.wang.my_community.model.User;
import org.apache.ibatis.session.RowBounds;
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

    public PaginationDto list(Integer page, Integer size) {


        PaginationDto paginationDto = new PaginationDto();
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        paginationDto.setPagination(totalCount, page, size);

        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));

        List<QuestionDto> questionDtos = new ArrayList<>();

        for (Question question : questions) {
          User user = userMapper.selectByPrimaryKey(question.getCreator());
           QuestionDto questionDto = new QuestionDto();
           BeanUtils.copyProperties(question,questionDto);
           questionDto.setUser(user);
           questionDtos.add(questionDto);
        }
        paginationDto.setQuestionDtos(questionDtos);
        return paginationDto;
    }

    public PaginationDto list(Integer userId, Integer page, Integer size) {
        PaginationDto paginationDto = new PaginationDto();

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);
        paginationDto.setPagination(totalCount,page,size);

        Integer offset = size * (page - 1);
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        List<QuestionDto> questionDtos = new ArrayList<>();

        for (Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        paginationDto.setQuestionDtos(questionDtos);
        return paginationDto;
    }

    public QuestionDto getById(Integer id) {

        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);

        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){

            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
            Question updateQuestion = new Question();
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andCreatorEqualTo(question.getId());
            questionMapper.updateByExampleSelective(updateQuestion, example);
        }
    }
}
