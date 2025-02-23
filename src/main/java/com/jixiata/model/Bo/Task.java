package com.jixiata.model.Bo;

public class Task {
    private String keyId;
    private String classId;
    private String startTime;
    private String endTime;
    private String taskName;
    private String content;
    private String remark;
    private String addTime;
    private String modifyTime;
    private Integer isDelete;
    private Integer submitCount;
    private Integer isNeedSubmit;
    private String finishedUsers;

    public Integer getIsNeedSubmit() {
        return isNeedSubmit;
    }

    public void setIsNeedSubmit(Integer isNeedSubmit) {
        this.isNeedSubmit = isNeedSubmit;
    }

    public String getFinishedUsers() {
        return finishedUsers;
    }

    public void setFinishedUsers(String finishedUsers) {
        this.finishedUsers = finishedUsers;
    }

    public Integer getSubmitCount() {
        return submitCount;
    }

    public void setSubmitCount(Integer submitCount) {
        this.submitCount = submitCount;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
