package com.jixiata.util;

import com.jixiata.dao.MessageMapper;
import com.jixiata.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用容器管理的，用于WebSocket的工具类
 */
@Component("webScoketUtils")
public class WebSocketUtils {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    public UserMapper getUserMapper() {
        return userMapper;
    }

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public MessageMapper getMessageMapper() {
        return messageMapper;
    }

    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }
}
