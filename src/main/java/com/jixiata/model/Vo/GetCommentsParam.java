package com.jixiata.model.Vo;

import com.jixiata.model.Bo.Message;

public class GetCommentsParam extends Message {

    /**
     * 查询类型: 1.按时间排序 2.按热度排序
     */
    private Integer searchType;

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }
}
