package com.jixiata.dao;

import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper {


    /**
     * 插入认证表数据
     * @param task
     * @return
     */
     Integer insertTask(Task task);

    /**
     * 通过用户名获取权限信息
     * @return
     */
     List<Task> getTaskByCondition(@Param("condition") Task task, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    /**
     * 通过AuthId更新权限信息
     * @param data
     * @return
     */
    Integer updateTaskByTaskId(Task data);

    /**
     * 根据Condition获取数量
     * @param task
     * @return
     */
    Integer getCountByCondition(Task task);
}
