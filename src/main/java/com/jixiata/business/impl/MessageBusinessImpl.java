package com.jixiata.business.impl;

import com.jixiata.business.IMessageBusiness;
import com.jixiata.dao.ChatGroupMapper;
import com.jixiata.model.Bo.ChatGroup;
import com.jixiata.model.Bo.Class;
import com.jixiata.dao.ClassMapper;
import com.jixiata.dao.MessageMapper;
import com.jixiata.dao.UserMapper;
import com.jixiata.model.Bo.Message;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.*;
import com.jixiata.util.CommonUtils;
import com.jixiata.util.ConstantEnum;
import com.jixiata.util.JedisPoolWriper;
import com.jixiata.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@SuppressWarnings("unchecked")
public class MessageBusinessImpl implements IMessageBusiness {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private JedisPoolWriper wriper;

    @Autowired
    private ChatGroupMapper chatGroupMapper;

    @Override
    public ResponseVo<List<CurrentSessionVo>> getMessageSession(String token) {
        User user = UserUtils.getCurrentUserByToken(wriper, token);
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        List<CurrentSessionVo> result = new ArrayList<>();
        // 0.班级群
        Class classCondition = new Class();
        classCondition.setKeyId(user.getClassId());
        List<Class> classInfos = classMapper.getClassInfoByCondition(classCondition);
        if (classInfos == null || classInfos.size() == 0){
            throw new RuntimeException("未查询到该用户班级信息");
        }
        CurrentSessionVo csv = new CurrentSessionVo();
        csv.setType(2);
        csv.setGroupName("[班群]"+classInfos.get(0).getClassName());
        NoMarkNumAndTimeVo gvo = messageMapper.getLastTimeAndNoMarkNumberOfGroup(user.getKeyId(), user.getClassId());
        if (gvo != null) {
            csv.setNoMarkNumber(gvo.getNoMarkNumber());
            csv.setLastMessageTime(gvo.getLastTime());
        } else {
            csv.setNoMarkNumber(0);
        }
        result.add(csv);

        // 获取自定义群组
        ChatGroup condition = new ChatGroup();
        condition.setUsers(user.getKeyId());
        List<ChatGroup> groups = chatGroupMapper.getChatGroupByCondition(condition, 1, 50);
        for (ChatGroup group  : groups){
            CurrentSessionVo vo = new CurrentSessionVo();
            vo.setType(3);
            NoMarkNumAndTimeVo tv = messageMapper.getLastTimeAndNoMarkNumberOfGroup(user.getKeyId(), group.getKeyId());
            if (tv != null) {
                vo.setNoMarkNumber(tv.getNoMarkNumber());
                vo.setLastMessageTime(tv.getLastTime());
            } else {
                vo.setNoMarkNumber(0);
            }
            vo.setGroupName(group.getGroupName());
            vo.setGroupId(group.getKeyId());
            result.add(vo);
        }

        // 1.根据当前用户为From或To查询聊天记录来获取存在会话列表 (此处只查询单对单）
       List<String> res = messageMapper.getMineExistMessageUser(user.getKeyId());
        for (String s: res) {
            CurrentSessionVo sessionVo = new CurrentSessionVo();
            sessionVo.setType(1);
            // 1.2 获取最后消息时间 以及未读数量
            NoMarkNumAndTimeVo vo =  messageMapper.getLastTimeAndNoMarkNumber(user.getKeyId(), s);
            sessionVo.setLastMessageTime(vo.getLastTime());
            sessionVo.setNoMarkNumber(vo.getNoMarkNumber());
            // 2.根据id获取用户信息组装返回
            User u = userMapper.getUserInfoByKeyId(s);
            sessionVo.setUser(u);
            result.add(sessionVo);
        }


        return ResponseVo.getResponseVo(true, result, ConstantEnum.SUCCESS.getStatusCode(), "查询成功");
    }

    @Override
    public ResponseVo<List<Message>> getNoMarkMessage(RequestVo<QueryNoMarkMessageVo> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (param.getData().getQueryType() == 2){
            param.getData().setGroupId(user.getClassId());
        }
         List<Message> messages = messageMapper.queryNoMarkMessage(param.getData());
        return  ResponseVo.getResponseVo(true,messages,ConstantEnum.SUCCESS.getStatusCode(),"查询成功");
    }

    @Override
    public ResponseVo<Boolean> updateNoMarkMessage(RequestVo<QueryNoMarkMessageVo> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }  // 更新消息
        Integer rows = messageMapper.updateNoMarkingMessages(param.getData());
        if (rows < 1){
            throw new RuntimeException("没有更新到未读消息");
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<ChatGroup> createChatGroup(RequestVo<String> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        // 选择的人，不包含用户自己
        String[] choiceUsers = param.getData().split("\\|");
        // 1.判断是否存在当前期望创建的群组
        ChatGroup condition = new ChatGroup();
        condition.setUsers(user.getKeyId());
        List<ChatGroup> groups = chatGroupMapper.getChatGroupByCondition(condition, 1, 50);
        ChatGroup result = null;
        for (int i = 0; i < groups.size(); i++){
            if (result != null){
                break;
            }
            // 判断群组是否存在
            String[] users = groups.get(i).getUsers().split("\\|");
            Set<String> userSet = new HashSet<String>(Arrays.asList(users));
            if (users.length == choiceUsers.length + 1){
                // 首先满足数量相当
                for (int j = 0; j < choiceUsers.length; j++){
                    if (!userSet.contains(choiceUsers[j])){
                        break;
                    }
                    if (j == choiceUsers.length - 1){
                        // 当为最后一个时候判断是否也包含创建者
                        if (userSet.contains(user.getKeyId())){
                            result = groups.get(i);
                        }
                    }
                }
            }
        }

        if (result == null){
            // 新建群组
            ChatGroup addGroup = new ChatGroup();
            addGroup.setKeyId(CommonUtils.getKeyID());
            addGroup.setCreater(user.getKeyId());
            addGroup.setGroupName(user.getName()+"发起的"+(choiceUsers.length+1)+"人群聊");
            addGroup.setUsers(param.getData()+user.getKeyId());
            addGroup.setAddTime(CommonUtils.getCurrentDateString());
            addGroup.setCount(choiceUsers.length+1);
            addGroup.setModifyTime(CommonUtils.getCurrentDateString());
            addGroup.setIsDelete(0);
            Integer rows = chatGroupMapper.insertChatGroup(addGroup);
            if (rows == 0){
                throw new RuntimeException("插入群组数据失败！");
            }
            result = addGroup;
        }
        return ResponseVo.getResponseVo(true,result,ConstantEnum.SUCCESS.getStatusCode(),"成功");
    }

    @Override
    public ResponseVo<List<User>> getGroupMembers(RequestVo<String> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        String groupId = param.getData();
        ChatGroup condition = new ChatGroup();
        condition.setKeyId(groupId);
        List<ChatGroup> groups = chatGroupMapper.getChatGroupByCondition(condition, 1, 1);
        if (groups == null || groups.size() == 0){
            throw new RuntimeException("未获取到相关群组");
        }
        ChatGroup chatGroup = groups.get(0);
        String[] users = chatGroup.getUsers().split("\\|");
        List<User> result = new ArrayList<>();
        for (String uid : users){
            User userInfo = userMapper.getUserInfoByKeyId(uid);
            result.add(userInfo);
        }
        return ResponseVo.getResponseVo(true, result, ConstantEnum.SUCCESS.getStatusCode(),"查询成功");
    }

}
