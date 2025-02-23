package com.jixiata.dao;

import com.jixiata.model.Bo.Task;
import com.jixiata.model.Bo.TaskResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskResultMapper {

    Integer updateTaskResultByKeyId(TaskResult result);

    Integer insertTaskResult(TaskResult result);

    List<TaskResult> getTaskResultByCondition(@Param("condition") TaskResult condition, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    Integer getCountByCondition(TaskResult result);

}
