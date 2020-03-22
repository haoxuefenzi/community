package com.wang.my_community.service;

import com.wang.my_community.dto.NotificationDto;
import com.wang.my_community.dto.PaginationDto;
import com.wang.my_community.dto.QuestionDto;
import com.wang.my_community.enums.NotificationEnum;
import com.wang.my_community.enums.NotificationTypeEnum;
import com.wang.my_community.mapper.NotificationMapper;
import com.wang.my_community.mapper.UserMapper;
import com.wang.my_community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDto<NotificationDto> list(Long userId, Integer page, Integer size) {
        PaginationDto<NotificationDto> paginationDto = new PaginationDto<>();

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);
        paginationDto.setPagination(totalCount,page,size);

        Integer offset = size * (page - 1);
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        if (notifications.size()==0){
            return paginationDto;
        }

        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto();
            BeanUtils.copyProperties(notification,notificationDto);
            notificationDto.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDtos.add(notificationDto);
        }
        paginationDto.setData(notificationDtos);
        return paginationDto;
    }

    public Long unreadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    public NotificationDto read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);


        notification.setStatus(NotificationEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDto notificationDto = new NotificationDto();
        BeanUtils.copyProperties(notification,notificationDto);
        notificationDto.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));

        return notificationDto;
    }
}
