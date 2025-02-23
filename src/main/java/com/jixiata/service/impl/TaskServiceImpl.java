package com.jixiata.service.impl;

import com.jixiata.business.ITaskBusiness;
import com.jixiata.model.Bo.Task;
import com.jixiata.model.Bo.TaskResult;
import com.jixiata.model.Vo.*;
import com.jixiata.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private ITaskBusiness taskBusiness;

    @Override
    public ResponseVo<Boolean> addTaskByTeacher(RequestVo<Task> param) {
        if (param == null || param.getData() == null ||StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try{
            return taskBusiness.addTaskByTeacher(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("添加作业异常:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<TasksResult> getTasks(RequestVo<Task> param) {
        if (param == null || StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        try {
            return taskBusiness.getTasks(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取作业异常:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> updateTaskByTeacher(RequestVo<Task> param) {
        if (param == null || param.getData() == null ||StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try{
            return taskBusiness.updateTaskByTeacher(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("更新作业异常:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> submitTaskResultByStudent(RequestVo<TaskResult> param) {
        if (param == null || param.getData() == null ||StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try{
            return taskBusiness.submitTaskResultByStudent(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("提交作业结果异常:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> updateTaskResult(RequestVo<TaskResult> param) {
        if (param == null || param.getData() == null ||StringUtils.isEmpty(param.getToken())
                || StringUtils.isEmpty(param.getData().getKeyId())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try{
            return taskBusiness.updateTaskResult(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("更新作业结果异常:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<TaskResultVo>> getTaskResult(RequestVo<TaskResult> param) {
        if (param == null  ||StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try{
            return taskBusiness.getTaskResult(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("查询作业结果异常:"+e.getMessage());
        }
    }
}
