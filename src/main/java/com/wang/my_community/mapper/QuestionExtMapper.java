package com.wang.my_community.mapper;

import com.wang.my_community.model.Question;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionExtMapper {
    int incView(Question record);
}
