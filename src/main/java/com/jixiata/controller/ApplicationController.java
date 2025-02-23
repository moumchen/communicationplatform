package com.jixiata.controller;

import com.jixiata.model.Bo.Application;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Api("审批（申请）相关接口")
@RequestMapping("/api")
public class ApplicationController {

    @Autowired
    private IApplicationService applicationService;

    //新建申请
    @PostMapping("/insertApplcationByStudent")
    @ApiOperation("新建申请（学生）")
    @ResponseBody
    public ResponseVo<Boolean> insertApplcationByStudent(@RequestBody RequestVo<Application> param){
        return applicationService.insertApplcationByStudent(param);
    }
    //    查询申请
    @PostMapping("/getApplication")
    @ApiOperation("查询申请")
    @ResponseBody
    public ResponseVo<List<Application>> getApplication(@RequestBody String param){
        return applicationService.getApplication(param);
    }
    //  申请操作（更新事务）
    @PostMapping("/updateApplication")
    @ApiOperation("申请操作（更新事务）")
    @ResponseBody
    public ResponseVo<Boolean> updateApplication(@RequestBody RequestVo<Application> param){
        return applicationService.updateApplication(param);
    }
}
