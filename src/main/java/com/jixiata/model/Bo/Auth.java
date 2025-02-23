package com.jixiata.model.Bo;

/**
 * 权限实体类
 * @date 2020.2.2
 */
public class Auth {
    private String keyId;
    private String username;
    private String password;
    private String salt;
    private String addTime;
    private String modifyTime;
    private Integer isLock;
    private Integer isDelete;
    private Integer source;

    public String getKeyId() {
        return keyId;
    }

    public Auth setKeyId(String keyId) {
        this.keyId = keyId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Auth setUsername(String username) {
        this.username = username;return this;
    }

    public String getPassword() {
        return password;
    }

    public Auth setPassword(String password) {
        this.password = password;return this;
    }

    public String getSalt() {
        return salt;
    }

    public Auth setSalt(String salt) {
        this.salt = salt;return this;
    }

    public String getAddTime() {
        return addTime;
    }

    public Auth setAddTime(String addTime) {
        this.addTime = addTime;return this;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public Auth setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;return this;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public Auth setIsLock(Integer isLock) {
        this.isLock = isLock;return this;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public Auth setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;return this;
    }

    public Integer getSource() {
        return source;
    }

    public Auth setSource(Integer source) {
        this.source = source;return this;
    }
}
