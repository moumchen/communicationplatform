package com.jixiata.model.Bo;

/**
 * 班级实体类
 */
public class Class {
    private String keyId;
    private String className;
    private String schoolName;
    private Integer kind;
    private String students;
    private String addTime;
    private String modifyTime;
    private Integer isDelete;
    private String inviteCode;
    private String owner;
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getKeyId() {
        return keyId;
    }

    public Class setKeyId(String keyId) {
        this.keyId = keyId;return this;
    }

    public String getClassName() {
        return className;
    }

    public Class setClassName(String className) {
        this.className = className;return this;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public Class setSchoolName(String schoolName) {
        this.schoolName = schoolName;return this;
    }

    public Integer getKind() {
        return kind;
    }

    public Class setKind(Integer kind) {
        this.kind = kind;return this;
    }

    public String getStudents() {
        return students;
    }

    public Class setStudents(String students) {
        this.students = students;return this;
    }

    public String getAddTime() {
        return addTime;
    }

    public Class setAddTime(String addTime) {
        this.addTime = addTime;return this;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public Class setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;return this;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public Class setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;return this;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public Class setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
        return this;
    }
}
