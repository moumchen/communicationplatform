package com.jixiata.model.Vo;

public class QueryNoMarkMessageVo {
    /**
     * 查询类型：1为单聊；2为班群聊天; 3为自定义群组聊天
     */
    private Integer queryType;

    /**
     * 当查询类型为1时，需要提供对方的keyId
     */
    private String oppoUserId;

    private String mineUserId;

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMineUserId() {
        return mineUserId;
    }

    public void setMineUserId(String mineUserId) {
        this.mineUserId = mineUserId;
    }

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public String getOppoUserId() {
        return oppoUserId;
    }

    public void setOppoUserId(String oppoUserId) {
        this.oppoUserId = oppoUserId;
    }
}
