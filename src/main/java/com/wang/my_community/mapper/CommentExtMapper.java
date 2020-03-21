package com.wang.my_community.mapper;

import com.wang.my_community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
    int incLikeCount(Comment comment);
    int decLikeCount(Comment comment);
}
