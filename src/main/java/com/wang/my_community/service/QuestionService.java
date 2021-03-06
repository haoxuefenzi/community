package com.wang.my_community.service;


import com.wang.my_community.dto.PaginationDto;
import com.wang.my_community.dto.QuestionDto;
import com.wang.my_community.dto.QuestionQueryDto;
import com.wang.my_community.excption.CustomizeErrorCode;
import com.wang.my_community.excption.CustomizeException;
import com.wang.my_community.mapper.QuestionExtMapper;
import com.wang.my_community.mapper.QuestionMapper;
import com.wang.my_community.mapper.UserMapper;
import com.wang.my_community.model.Question;
import com.wang.my_community.model.QuestionExample;
import com.wang.my_community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDto list(String search,Integer page, Integer size) {
        if (!StringUtils.isEmpty(search)){
            String searched = StringUtils.replace(search, " ", "|");
        }
        PaginationDto paginationDto = new PaginationDto();
        QuestionQueryDto questionQueryDto = new QuestionQueryDto();
        questionQueryDto.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDto);
        paginationDto.setPagination(totalCount, page, size);

        Integer offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_create desc");
        questionQueryDto.setPage(offset);
        questionQueryDto.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDto);

        List<QuestionDto> questionDtos = new ArrayList<>();

        for (Question question : questions) {
          User user = userMapper.selectByPrimaryKey(question.getCreator());
           QuestionDto questionDto = new QuestionDto();
           BeanUtils.copyProperties(question,questionDto);
           questionDto.setUser(user);
           questionDtos.add(questionDto);
        }
        paginationDto.setData(questionDtos);
        return paginationDto;
    }

    public PaginationDto list(Long userId, Integer page, Integer size) {
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
        paginationDto.setData(questionDtos);
        return paginationDto;
    }

    public QuestionDto getById(Long id) {

        Question question = questionMapper.selectByPrimaryKey(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
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
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }else {
            Question updateQuestion = new Question();
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andCreatorEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (i==0){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }

    public List<QuestionDto> selectRelated(QuestionDto questionDto) {
        if (StringUtils.isEmpty(questionDto.getTag())){
            return new ArrayList<>();
        }
        String replace = StringUtils.replace(questionDto.getTag(), ",", "|");
        Question question = new Question();
        question.setId(questionDto.getId());
        question.setTag(replace);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDto> collect = questions.stream().map(q -> {
            QuestionDto queryDto = new QuestionDto();
            BeanUtils.copyProperties(q, queryDto);
            return queryDto;
        }).collect(Collectors.toList());
        return collect;
    }
}
