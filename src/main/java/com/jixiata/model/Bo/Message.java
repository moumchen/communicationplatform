package com.jixiata.model.Bo;

public class Message {

    private String keyId;
    private String fromUser;
    private String toUser;
    private Integer type;
    private String content;
    private Integer isRead;
    private Integer isDelete;
    private String addTime;
    private String modifyTime;
    private String groupId;


    // 搜索用
    private String startAddTime;
    private String endAddTime;


    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getStartAddTime() {
        return startAddTime;
    }

    public void setStartAddTime(String startAddTime) {
        this.startAddTime = startAddTime;
    }

    public String getEndAddTime() {
        return endAddTime;
    }

    public void setEndAddTime(String endAddTime) {
        this.endAddTime = endAddTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
