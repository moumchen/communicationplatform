package com.jixiata.service.impl;

import com.jixiata.business.IMessageBusiness;
import com.jixiata.model.Bo.ChatGroup;
import com.jixiata.model.Bo.Message;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.CurrentSessionVo;
import com.jixiata.model.Vo.QueryNoMarkMessageVo;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private IMessageBusiness messageBusiness;

    @Override
    public ResponseVo<List<CurrentSessionVo>> getMessageSession(String token) {
        if (StringUtils.isEmpty(token)){
            ResponseVo.getParamErrorResponseVo();
        }
        try{
            return messageBusiness.getMessageSession(token);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取用户默认聊天列表错误:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<Message>> getNoMarkMessage(RequestVo<QueryNoMarkMessageVo> param) {
        if (null == param || StringUtils.isEmpty(param.getToken())){
            ResponseVo.getParamErrorResponseVo();
        }
        try{
            return messageBusiness.getNoMarkMessage(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取未读消息错误:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> updateNoMarkMessage(RequestVo<QueryNoMarkMessageVo> param) {
        if (null == param || StringUtils.isEmpty(param.getToken())){
            ResponseVo.getParamErrorResponseVo();
        }
        try{
            return messageBusiness.updateNoMarkMessage(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("更新未读状态为已读错误:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<ChatGroup> createChatGroup(RequestVo<String> param) {
        if (null == param || StringUtils.isEmpty(param.getToken()) || StringUtils.isEmpty(param.getData())){
            ResponseVo.getParamErrorResponseVo();
        }
        try{
            return messageBusiness.createChatGroup(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("自定义群组:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<User>> getGroupMembers(RequestVo<String> param) {
        if (null == param || StringUtils.isEmpty(param.getToken()) || StringUtils.isEmpty(param.getData())){
            ResponseVo.getParamErrorResponseVo();
        }
        try{
            return messageBusiness.getGroupMembers(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取群组成员信息:"+e.getMessage());
        }
    }
}
