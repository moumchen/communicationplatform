package com.jixiata.service;

import com.jixiata.model.Bo.Task;
import com.jixiata.model.Bo.TaskResult;
import com.jixiata.model.Vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITaskService {
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
     * 更新作业信息
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateTaskByTeacher(RequestVo<Task> param);


    /**
     * 提交作业结果（学生）
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
     * 获取作业结果信息
     * （根据TaskId以及UserId查询某一用户作业信息 或者 根据TaskId查询某作业全部结果信息）
     * @param param
     * @return
     */
    ResponseVo<List<TaskResultVo>> getTaskResult(RequestVo<TaskResult> param);
}
