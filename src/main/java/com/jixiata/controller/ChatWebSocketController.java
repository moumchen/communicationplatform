package com.jixiata.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jixiata.dao.ChatGroupMapper;
import com.jixiata.dao.ClassMapper;
import com.jixiata.dao.MessageMapper;
import com.jixiata.dao.UserMapper;
import com.jixiata.model.Bo.ChatGroup;
import com.jixiata.model.Bo.Message;
import com.jixiata.model.Bo.User;
import com.jixiata.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/chat/{userId}")
public class ChatWebSocketController {


    private static MessageMapper messageMapper;
    @Autowired
    public void setMessageMapper(MessageMapper messageMapper){
        ChatWebSocketController.messageMapper = messageMapper;
    }

    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        ChatWebSocketController.userMapper = userMapper;
    }

    private static ChatGroupMapper chatGroupMapper;
    @Autowired
    public void setChatGroupMapper(ChatGroupMapper chatGroupMapper){
        ChatWebSocketController.chatGroupMapper = chatGroupMapper;
    }

    private Session msession;
    private String userId;
    /**
     * 保存用户会话的Map集合
     */
    private static ConcurrentHashMap<String,Session> userSessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("userId")String userId, Session session) throws IOException {
        // 将会话保存到Map集合中
        this.userId = userId;
        this.msession = session;
        userSessionMap.put(userId,session);
        System.out.println("用户ID为"+userId+"的用户进入会话间!");
    }

    /**
     * 会话格式: json
     * 包含内容: Message
     * @param message
     * @param session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        Message msg = JSON.parseObject(message, Message.class);
        // 会话类型为单聊
        if (msg.getType() == 1) {
            // 给消息设置共公属性
            msg.setKeyId(CommonUtils.getKeyID());
            msg.setAddTime(CommonUtils.getCurrentDateString());
            msg.setModifyTime(CommonUtils.getCurrentDateString());
            msg.setIsDelete(0);
            // 判断对方用户是否在会话中
            Session oppoSession = userSessionMap.get(msg.getToUser());
            if (oppoSession == null) {
                // 存储到表，标记为未读
                msg.setIsRead(0);
            } else {
                // 发送给对方，标记为已读
                msg.setIsRead(1);
                oppoSession.getBasicRemote().sendText(JSON.toJSONString(msg));
            }
            // 持久化
            Integer row = messageMapper.insertMessage(msg);
            if (row < 1) {
                msession.getBasicRemote().sendText("ERROR:消息保存错误");
            }
        } else if (msg.getType() == 2) {
            // 群聊
            // 根据传入的GroupId即classId获取班级成员
            List<User> list = new ArrayList<>();
            List<User> temp;
            User user = new User();
            user.setClassId(msg.getGroupId());
            Integer pageIndex = 1;
            do {
                temp = userMapper.getUserInfoByCondition(user, pageIndex, 50);
                pageIndex++;
                list.addAll(temp);
            } while (temp.size() == 50);
            sendMsg(list, 2, msg);
        } else {
            // 自定义群组
            // 群聊
            String groupId = msg.getGroupId();
            ChatGroup condition = new ChatGroup();
            condition.setKeyId(groupId);
            List<ChatGroup> chatgroups = chatGroupMapper.getChatGroupByCondition(condition, 1, 1);
            ChatGroup group = chatgroups.get(0);
            String[] userIds = group.getUsers().split("\\|");
            List<User> users = new ArrayList<>();
            for (int i = 0; i < userIds.length; i++) {
                User userInfo = userMapper.getUserInfoByKeyId(userIds[i]);
                users.add(userInfo);
            }
            sendMsg(users, 3, msg);
        }
    }

    private void sendMsg(List<User> list, Integer type, Message msg) throws IOException {
        Message msgTemp = new Message();
        msgTemp.setIsDelete(0);
        msgTemp.setContent(msg.getContent());
        msgTemp.setFromUser(msg.getFromUser());
        msgTemp.setGroupId(msg.getGroupId());
        msgTemp.setType(type);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getKeyId().equals(userId)) {
                continue;
            }
            msgTemp.setKeyId(CommonUtils.getKeyID());
            msgTemp.setAddTime(CommonUtils.getCurrentDateString());
            msgTemp.setModifyTime(CommonUtils.getCurrentDateString());
            msgTemp.setToUser(list.get(i).getKeyId());
            Session sessiomTemp = userSessionMap.get(list.get(i).getKeyId());
            if (sessiomTemp == null) {
                // 持久化为未读消息
                msgTemp.setIsRead(0);
            } else {
                msgTemp.setIsRead(1);
                // 发送消息
                sessiomTemp.getBasicRemote().sendText(JSON.toJSONString(msgTemp));
            }
            messageMapper.insertMessage(msgTemp);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("用户ID为"+userId+"的用户退出会话间!");
        userSessionMap.remove(userId);
    }

    /**
     * 错误-1代码 非正常流程关闭房间
     */
    private void onErrorClose() throws IOException {
        System.out.println("用户ID为"+userId+"的用户退出会话间!");
        userSessionMap.remove(userId);
    }

    @OnClose
    public void onClose() throws IOException {
        System.out.println("用户ID为"+userId+"的用户退出会话间!");
        userSessionMap.remove(userId);
    }
}
