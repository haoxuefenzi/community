package com.wang.my_community.service;

import com.wang.my_community.dto.CommentDto;
import com.wang.my_community.enums.CommentTypeEnum;
import com.wang.my_community.enums.NotificationTypeEnum;
import com.wang.my_community.enums.NotificationEnum;
import com.wang.my_community.excption.CustomizeErrorCode;
import com.wang.my_community.excption.CustomizeException;
import com.wang.my_community.mapper.*;
import com.wang.my_community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    public void insert(Comment comment, User commentator) {
        if (comment.getCommentCount()==null){
            comment.setCommentCount(0);
        }
        if (comment.getParentId() == null) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExit(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
//            增加评论数
            dbComment.setCommentCount(1);
            commentExtMapper.incCommentCount(dbComment);
            //创建评论评论的通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), commentator.getName(), NotificationTypeEnum.REPLY_COMMENT,question.getId());
        } else {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建评论问题的通知
            createNotify(comment, question.getCreator(), commentator.getName(),question.getTitle(), NotificationTypeEnum.REPLY_QUESTION,question.getId());
        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String title, NotificationTypeEnum notificationTypeEnum, Long outerId) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(title);
        notificationMapper.insert(notification);
    }

    public Comment selectById(Long id){
        Comment comment = commentMapper.selectByPrimaryKey(id);
        return comment;
    }

    public List<CommentDto> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size()==0){
            return new ArrayList<>();
        }
        //去重的评论者
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());

        //将评论者放入map集合
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(new ArrayList<>(commentators));
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //将comment转换为commentDto,即放入user
        List<CommentDto> commentDtos = comments.stream().map(comment -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment, commentDto);
            commentDto.setUser(userMap.get(comment.getCommentator()));
            return commentDto;
        }).collect(Collectors.toList());
        return commentDtos;
    }
}
