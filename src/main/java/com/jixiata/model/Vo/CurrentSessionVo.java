package com.jixiata.model.Vo;

import com.jixiata.model.Bo.User;

/**
 * 当前用户默认会话列表 ( 存在聊天记录的会话 ）
 */
public class CurrentSessionVo {

    private User user;
    private String groupName;
    private String groupId;
    private Integer type;
    private Integer noMarkNumber;
    private String lastMessageTime;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNoMarkNumber() {
        return noMarkNumber;
    }

    public void setNoMarkNumber(Integer noMarkNumber) {
        this.noMarkNumber = noMarkNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
