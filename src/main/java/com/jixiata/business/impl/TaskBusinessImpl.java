package com.jixiata.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jixiata.business.ITaskBusiness;
import com.jixiata.dao.TaskMapper;
import com.jixiata.dao.TaskResultMapper;
import com.jixiata.dao.UserMapper;
import com.jixiata.model.Bo.Exam;
import com.jixiata.model.Bo.Task;
import com.jixiata.model.Bo.TaskResult;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.*;
import com.jixiata.util.CommonUtils;
import com.jixiata.util.ConstantEnum;
import com.jixiata.util.JedisPoolWriper;
import com.jixiata.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.management.relation.RoleUnresolved;
import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class TaskBusinessImpl implements ITaskBusiness {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private JedisPoolWriper wriper;

    @Autowired
    private TaskResultMapper taskResultMapper;

    @Autowired
    private UserMapper userMapper;
    private String FILE_ADDRESS = this.getClass().getResource("/").getPath();
    @Override
    public ResponseVo<Boolean> addTaskByTeacher(RequestVo<Task> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null) {
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (user.getIdentity() != 1){
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        Task task = param.getData();
        task.setKeyId(CommonUtils.getKeyID());
        task.setAddTime(CommonUtils.getCurrentDateString());
        task.setModifyTime(CommonUtils.getCurrentDateString());
        task.setClassId(user.getClassId());
        task.setIsDelete(0);
        Integer rows = taskMapper.insertTask(task);
        if (rows < 1){
            throw new RuntimeException("插入数据异常");
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<TasksResult> getTasks(RequestVo<Task> param) {
        // 依然需要是登录用户
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        TasksResult result = new TasksResult();
        List<Task> list;
        Integer totalCount = 0;
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        Task condition = new Task();
        if (param.getData() != null){
            // 如果请求Data不为空，根据Data查询
            condition = param.getData();
        } else{
            // 根据当前用户班级查
            condition.setClassId(user.getClassId());
        }
        totalCount = taskMapper.getCountByCondition(condition);
        list = taskMapper.getTaskByCondition(condition, param.getPageIndex(), param.getPageSize());
        if (CollectionUtils.isEmpty(list)){
            list = new ArrayList<>();
        }
        result.setList(list);
        result.setTotalCount(totalCount);
        return ResponseVo.getResponseVo(true,result, ConstantEnum.SUCCESS.getStatusCode(),"查询成功");
    }

    @Override
    public ResponseVo<Boolean> updateTaskByTeacher(RequestVo<Task> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null) {
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (user.getIdentity() != 1){
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        Integer rows = taskMapper.updateTaskByTaskId(param.getData());
        if (rows < 1){
            throw new RuntimeException("更新任务数据异常");
        }
        return ResponseVo.getResponseVo(true,true,ConstantEnum.SUCCESS.getStatusCode(),"更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<Boolean> submitTaskResultByStudent(RequestVo<TaskResult> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null) {
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (user.getIdentity() != 0){
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        String path = FILE_ADDRESS.substring(1,FILE_ADDRESS.length()).replaceAll("/","\\\\").replace("WEB-INF\\classes","resources\\images\\taskResult");
        TaskResult result = param.getData();
        result.setAddTime(CommonUtils.getCurrentDateString());
        result.setFile(path + result.getFile());
        result.setIsDelete(0);
        result.setCount(1);
        result.setModifyTime(CommonUtils.getCurrentDateString());
        result.setIsLock(0);
        result.setKeyId(CommonUtils.getKeyID());
        result.setUserId(user.getKeyId());
        result.setScore(0);

        Integer row = taskResultMapper.insertTaskResult(result);
        if (row == 0){
            throw new RuntimeException("插入作业结果数据失败");
        }

        // 更新作业表信息
        Task condition = new Task();
        condition.setKeyId(param.getData().getTaskId());
        List<Task> tasks = taskMapper.getTaskByCondition(condition, 1, 1);
        Task task = tasks.get(0);
        task.setSubmitCount(task.getSubmitCount() + 1);
        Integer row2 = taskMapper.updateTaskByTaskId(task);
        if (row2 == 0){
            throw new RuntimeException("更新作业数据失败");
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<Boolean> updateTaskResult(RequestVo<TaskResult> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null) {
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        TaskResult result = param.getData();
        result.setModifyTime(CommonUtils.getCurrentDateString());
        Integer row = taskResultMapper.updateTaskResultByKeyId(result);
        if (row == 0){
            throw new RuntimeException("更新数据失败");
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<List<TaskResultVo>> getTaskResult(RequestVo<TaskResult> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null) {
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        TaskResult condition = param.getData();
        // 如果当前用户是学生用户，只查询该学生的结果信息
        if (user.getIdentity() == 0){
            condition.setUserId(user.getKeyId());
        }
        List<TaskResult> result = new ArrayList<>();
        List<TaskResult> temp ;
        int pageIndex = 1;
        do {
            temp = taskResultMapper.getTaskResultByCondition(condition, pageIndex, 50);
            result.addAll(temp);
            pageIndex++;
        } while (temp.size() == 50);

        List<TaskResultVo> resultVos = new ArrayList<>();
        // 根据查询出来的结果查用户信息
        if (result != null && result.size() > 0){
            for (TaskResult taskResult : result){
                TaskResultVo taskResultVo = JSON.parseObject(JSON.toJSONString(taskResult), TaskResultVo.class);
                String userId = taskResultVo.getUserId();
                User userInfo = userMapper.getUserInfoByKeyId(userId);
                taskResultVo.setUserName(userInfo.getName());
                resultVos.add(taskResultVo);
            }
        }
        return ResponseVo.getResponseVo(true,resultVos,ConstantEnum.SUCCESS.getStatusCode(),"查询成功");
    }


}
