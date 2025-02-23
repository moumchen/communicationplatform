package com.jixiata.business;

import com.jixiata.model.Bo.Task;
import com.jixiata.model.Bo.TaskResult;
import com.jixiata.model.Vo.*;

import java.util.List;

public interface ITaskBusiness {
    /**
     * 教师用户添加作业
     * @param param
     * @return
     */
    ResponseVo<Boolean> addTaskByTeacher(RequestVo<Task> param);

    /**
     * 获取作业信息
     * @param param
     * @return
     */
    ResponseVo<TasksResult> getTasks(RequestVo<Task> param);

    /**
     * 更新作业
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateTaskByTeacher(RequestVo<Task> param);

    /**
     * 提交作业结果
     * @param param
     * @return
     */
    ResponseVo<Boolean> submitTaskResultByStudent(RequestVo<TaskResult> param);

    /**
     * 更新作业结果信息
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateTaskResult(RequestVo<TaskResult> param);

    /**
     * 查询作业结果信息错误
     * @param param
     * @return
     */
    ResponseVo<List<TaskResultVo>> getTaskResult(RequestVo<TaskResult> param);
}
