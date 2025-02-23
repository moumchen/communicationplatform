package com.jixiata.controller;

import com.alibaba.fastjson.JSON;
import com.jixiata.model.Bo.Class;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 班级相关接口
 */
@Controller
@RequestMapping("/api")
@Api("班级相关接口")
public class ClassController {

    @Autowired
    private IClassService classService;

    @PostMapping("/addNewClassByTeacher")
    @ApiOperation("新建班级 (权限：教师)")
    @ResponseBody
    public ResponseVo<Class> addNewClassByTeacher(@RequestBody RequestVo<Class> param){
        return classService.addNewClass(param);
    }

    /**
     * 获取自己班级的班级信息
     * @param token
     * @return
     */
    @PostMapping("/getClassInfoBySelf")
    @ResponseBody
    @ApiOperation("获取当前用户的班级信息")
    public ResponseVo<Class> getClassInfoBySelf(@RequestBody String token){
        return classService.getClassInfoBySelf(token);
    }

    /**
     *更新班级信息 权限：教师
     * @param param
     * @return
     */
    @PostMapping("/updateClassInfoByTeacher")
    @ResponseBody
    @ApiOperation("更新班级信息（权限：教师）")
    public ResponseVo<Class> updateClassInfoByTeacher(@RequestBody RequestVo<Class> param){
        return classService.updateClassInfoByTeacher(param);
    }

    /**
     * 加入班级接口 权限：仅学生 未加入班级
     */
    @PostMapping("/joinClass")
    @ApiOperation("加入班级（权限：仅学生 未加入班级）")
    @ResponseBody
    public ResponseVo<Boolean> joinClass(@RequestBody RequestVo<String> param){
        return classService.updateUserClassAndClassInfo(param);
    }

    /**
     * 获取当前用户所在班级成员 （权限 拥有班级）
     * @param token
     * @return
     */
    @PostMapping("/getClassMemberBySelf")
    @ResponseBody
    @ApiOperation("获取当前用户所在班级成员")
    public ResponseVo<List<User>> getClassMemberBySelf(@RequestBody String token){
        return classService.getClassMemberBySelf(token);
    }

    /**
     * 获取当前用户所在班级成员 （权限 拥有班级）
     * @param param
     * @return
     */
    @PostMapping("/deleteClassMemberByTeacher")
    @ResponseBody
    @ApiOperation("移除班级成员")
    public ResponseVo<Boolean> deleteClassMemberByTeacher(@RequestBody RequestVo<String> param){
        return classService.deleteClassMemberByTeacher(param);
    }

    /**
     * 退出班级
     */
    @PostMapping("/updateStudentClassInfoByStudent")
    @ResponseBody
    @ApiOperation("退出班级 （仅学生）")
    public ResponseVo<Boolean> updateStudentClassInfoByStudent(@RequestBody String token){
        return classService.updateStudentClassInfoByStudent(token);
    }
}
