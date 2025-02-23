package com.jixiata.controller;

import com.jixiata.model.Bo.ChatGroup;
import com.jixiata.model.Bo.Message;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.CurrentSessionVo;
import com.jixiata.model.Vo.QueryNoMarkMessageVo;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Api("消息相关API")
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private IMessageService messageService;

    @PostMapping("/getMessageSession")
    @ResponseBody
    @ApiOperation("获取当前用户会话列表")
    public ResponseVo<List<CurrentSessionVo>> getMessageSession(@RequestBody String token){
        return messageService.getMessageSession(token);
    }

    @PostMapping("/getNoMarkMessage")
    @ResponseBody
    @ApiOperation("获取未读消息")
    public ResponseVo<List<Message>> getNoMarkMessage(@RequestBody RequestVo<QueryNoMarkMessageVo> param){
        return messageService.getNoMarkMessage(param);
    }
    @PostMapping("/updateNoMarkMessage")
    @ResponseBody
    @ApiOperation("更新未读消息状态为已读")
    public ResponseVo<Boolean> updateNoMarkMessage(@RequestBody RequestVo<QueryNoMarkMessageVo> param){
        return messageService.updateNoMarkMessage(param);
    }

    @PostMapping("/createChatGroup")
    @ResponseBody
    @ApiOperation("自定义群组")
    public ResponseVo<ChatGroup> createChatGroup(@RequestBody RequestVo<String> param){
        return messageService.createChatGroup(param);
    }

    @PostMapping("/getGroupMembers")
    @ResponseBody
    @ApiOperation("获取群组成员信息")
    public ResponseVo<List<User>> getGroupMembers(@RequestBody RequestVo<String> param){
        return messageService.getGroupMembers(param);
    }
}
