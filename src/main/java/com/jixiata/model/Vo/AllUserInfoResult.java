package com.jixiata.model.Vo;

import java.util.List;

public class AllUserInfoResult {
    private List<CompleteUserInfoResult> result;
    private Integer totalCount;


    public List<CompleteUserInfoResult> getResult() {
        return result;
    }

    public void setResult(List<CompleteUserInfoResult> result) {
        this.result = result;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
