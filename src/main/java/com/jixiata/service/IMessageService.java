package com.jixiata.service;

import com.jixiata.model.Bo.ChatGroup;
import com.jixiata.model.Bo.Message;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.CurrentSessionVo;
import com.jixiata.model.Vo.QueryNoMarkMessageVo;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;

import java.util.List;

public interface IMessageService {
    /**
     * 获取当前用户默认的用户会话列表（存在聊天记录）
     * @param token
     * @return
     */
    ResponseVo<List<CurrentSessionVo>> getMessageSession(String token);

    /**
     * 获取未读消息
     * @param param
     * @return
     */
    ResponseVo<List<Message>> getNoMarkMessage(RequestVo<QueryNoMarkMessageVo> param);

    /**
     * 更新未读状态为已读
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateNoMarkMessage(RequestVo<QueryNoMarkMessageVo> param);

    /**
     * 自定义群组
     * @param param
     * @return
     */
    ResponseVo<ChatGroup> createChatGroup(RequestVo<String> param);

    /**
     * 获取自定义群组成员信息
     * @param param
     * @return
     */
    ResponseVo<List<User>> getGroupMembers(RequestVo<String> param);
}
