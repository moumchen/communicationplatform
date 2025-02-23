package com.jixiata.controller;

import com.jixiata.model.Bo.Task;
import com.jixiata.model.Bo.TaskResult;
import com.jixiata.model.Vo.*;
import com.jixiata.service.ITaskService;
import com.jixiata.util.ConstantEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.taglibs.standard.lang.jstl.BooleanLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/api")
@Api("作业以及作业结果相关接口")
@SuppressWarnings("unchecked")
public class TaskController {

    @Autowired
    private ITaskService taskService;

//    @Value("${FILE_ADDRESS}")
    private String FILE_ADDRESS = this.getClass().getResource("/").getPath();

    /**
     * 新建作业 （权限仅教师）
     */
    @PostMapping("/addTaskByTeacher")
    @ResponseBody
    @ApiOperation("新建作业 （仅教师）")
    public ResponseVo<Boolean> addTaskByTeacher(@RequestBody RequestVo<Task> param){
        return taskService.addTaskByTeacher(param);
    }
    /**
     * 查看作业
     * 注意：Task入参为空则根据当前用户获取，Task不为空则根据Task进行Condition
     * 建议分页查
     */
    @PostMapping("/getTasks")
    @ResponseBody
    @ApiOperation("查询作业")
    public ResponseVo<TasksResult> getTasks(@RequestBody RequestVo<Task> param){
        return taskService.getTasks(param);
    }
    /**
     * 修改作业
     */
    @PostMapping("/updateTaskByTeacher")
    @ResponseBody
    @ApiOperation("修改作业 （仅教师）")
    public ResponseVo<Boolean> updateTaskByTeacher(@RequestBody RequestVo<Task> param){
        return taskService.updateTaskByTeacher(param);
    }

    /**
     * 提交作业结果
     */
    @PostMapping("/submitTaskResultByStudent")
    @ResponseBody
    @ApiOperation("提交作业结果（学生）")
    public ResponseVo<Boolean> submitTaskResultByStudent(@RequestBody RequestVo<TaskResult> param){
        return taskService.submitTaskResultByStudent(param);
    }

    /**
     * 更新作业结果信息-------------作业结果打分（作业结果ID）
     */
    @PostMapping("/updateTaskResult")
    @ResponseBody
    @ApiOperation("更新作业结果信息")
    public ResponseVo<Boolean> updateTaskResult(@RequestBody RequestVo<TaskResult> param){
        return taskService.updateTaskResult(param);
    }

    /**
     * 查询作业结果
     */
    @PostMapping("/getTaskResult")
    @ResponseBody
    @ApiOperation("查询作业结果")
    public ResponseVo<List<TaskResultVo>> getTaskResult(@RequestBody RequestVo<TaskResult> param){
        return taskService.getTaskResult(param);
    }
    /*
    上传作业结果图片
     */
    @ResponseBody
    @PostMapping("/uploadTaskResultFile")
    @ApiOperation("上传作业结果文件")
        public ResponseVo<String> uploadTaskResultFile(MultipartFile file){
        String path = FILE_ADDRESS.substring(1,FILE_ADDRESS.length()).replaceAll("/","\\\\").replace("WEB-INF\\classes","resources\\images\\taskResult");
        /// / 文件处理
        if (file == null){
            return ResponseVo.getFailResponseVoByMessage("文件为空");
        }
        File pathFile = new File(path);
        if (!pathFile.exists()){
            pathFile.mkdir();
        }
        String originalFilename = file.getOriginalFilename();
        String type = originalFilename.substring(originalFilename.indexOf('.')+1, originalFilename.length());
        if ("jpeg".equals(type) || "jpg".equals(type) || "png".equals(type)){
            String filename = "TASKRESULT_"+System.currentTimeMillis()+"."+type;
            try {
                file.transferTo(new File(path+"\\"+filename));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseVo.getFailResponseVoByMessage("保存文件出错："+e.getMessage());
            }
            return ResponseVo.getResponseVo(true,filename, ConstantEnum.SUCCESS.getStatusCode(),"操作成功!");
        } else {
            return ResponseVo.getFailResponseVoByMessage("文件格式不支持!请上传以下格式文件:jpg/jpeg、png");
        }
    }
}
