package com.wang.my_community.controller;

import com.wang.my_community.dto.CommentCreateDto;
import com.wang.my_community.dto.CommentDto;
import com.wang.my_community.dto.ResultDto;
import com.wang.my_community.enums.CommentTypeEnum;
import com.wang.my_community.enums.NotificationTypeEnum;
import com.wang.my_community.enums.NotificationEnum;
import com.wang.my_community.excption.CustomizeErrorCode;
import com.wang.my_community.mapper.CommentExtMapper;
import com.wang.my_community.mapper.NotificationMapper;
import com.wang.my_community.model.Comment;
import com.wang.my_community.model.Notification;
import com.wang.my_community.model.User;
import com.wang.my_community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;


    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public ResultDto<Long> post(@RequestBody CommentCreateDto commentCreateDto,
                       HttpServletRequest request) {
        if (commentCreateDto.getGetterId() != null&&commentCreateDto.getMsg()==1){
            Comment comment = commentService.selectById(commentCreateDto.getGetterId());
            comment.setLikeCount(1L);
            commentExtMapper.incLikeCount(comment);

            Notification notification = new Notification();
            notification.setGmtCreate(System.currentTimeMillis());
            notification.setType(NotificationTypeEnum.GET_LIKE.getType());
            notification.setOuterId(comment.getParentId());
            notification.setNotifier(commentCreateDto.getSetterId());
            notification.setStatus(NotificationEnum.UNREAD.getStatus());
            notification.setReceiver(comment.getCommentator());
            notificationMapper.insert(notification);

            return ResultDto.okOf(commentService.selectById(commentCreateDto.getGetterId()).getLikeCount());
        } else if (commentCreateDto.getGetterId() != null&&commentCreateDto.getMsg()==0){
            Comment comment = commentService.selectById(commentCreateDto.getGetterId());
            comment.setLikeCount(1L);
            commentExtMapper.decLikeCount(comment);
            return ResultDto.okOf(commentService.selectById(commentCreateDto.getGetterId()).getLikeCount());
        }else {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
            }
            if (StringUtils.isEmpty(commentCreateDto.getContent())) {
                return ResultDto.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
            }
            Comment comment = new Comment();
            comment.setParentId(commentCreateDto.getParentId());
            comment.setContent(commentCreateDto.getContent());
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(System.currentTimeMillis());
            comment.setType(commentCreateDto.getType());
            comment.setCommentator(user.getId());
            comment.setLikeCount(0L);//加一个L就转成Long
            commentService.insert(comment,user);
            return ResultDto.okOf();
        }
    }



    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDto<CommentDto> comments(@PathVariable(name = "id") Long id){
        List<CommentDto> commentDtos = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);

        return ResultDto.okOf(commentDtos);
    }
}
