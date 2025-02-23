package com.jixiata.model.Vo;

import com.jixiata.model.Bo.Task;

import java.util.List;

public class TasksResult {
    private List<Task> list;
    private Integer totalCount;

    public List<Task> getList() {
        return list;
    }

    public void setList(List<Task> list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
