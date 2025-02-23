package com.jixiata.controller;

import com.jixiata.model.Bo.Exam;
import com.jixiata.model.Bo.Score;
import com.jixiata.model.Vo.ExamListResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.model.Vo.StudentAndScoreResult;
import com.jixiata.service.IExamService;
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
@RequestMapping("/api")
@Api("考试相关接口")
public class ExamController {

    @Autowired
    private IExamService examService;

    //新建考试
    @PostMapping("/addExamByTeacher")
    @ApiOperation("新建考试 （权限：教师）")
    @ResponseBody
    public ResponseVo<Exam> addExamByTeacher(@RequestBody RequestVo<Exam> param){
        return examService.addExamByTeacher(param);
    }

//    新建成绩（支持批量)
    @PostMapping("/addScoresByTeacher")
    @ApiOperation("批量新建成绩 （权限：教师）")
    @ResponseBody
    public ResponseVo<Boolean> addScoresByTeacher(@RequestBody RequestVo<List<Score>> param){
        return examService.addScoresByTeacher(param);
    }
//    修改考试
    @PostMapping("/updateExamInfoByTeacher")
    @ApiOperation("修改考试信息 （权限：教师）")
    @ResponseBody
    public ResponseVo<Boolean> updateExamInfoByTeacher(@RequestBody RequestVo<Exam> param){
        return examService.updateExamInfoByTeacher(param);
    }
//    修改成绩
    @PostMapping("/updateScoresByTeacher")
    @ApiOperation("批量修改成绩信息 （权限：教师）")
    @ResponseBody
    public ResponseVo<Boolean> updateScoresByTeacher(@RequestBody RequestVo<List<Score>> param){
        return examService.updateScoresByTeacher(param);
    }
//    查询当前用户考试列表（examid） 如果当前用户是学生，同时返回该学生本次考试结果
    @PostMapping("/getExamList")
    @ApiOperation("分页查询当前用户班级考试列表")
    @ResponseBody
    public ResponseVo<List<ExamListResult>> getExamList(@RequestBody RequestVo param){
        return examService.getExamList(param);
    }

    // 根据考试ID查询当前用户成绩（学生用）： 注意，存在该学生用户没有成绩的情况（缺考）
    @PostMapping("/getScoreByStudent")
    @ApiOperation("根据考试ID查询当前用户成绩（学生用）")
    @ResponseBody
    public ResponseVo<StudentAndScoreResult> getScoreByStudent(@RequestBody RequestVo<String> param){
        return examService.getScoreByStudent(param);
    }
    //根据考试ID查询所有学生成绩（教师用）
    @PostMapping("/getScoresByTeacher")
    @ApiOperation("根据考试ID查询所有学生成绩（教师用）")
    @ResponseBody
    public ResponseVo<List<StudentAndScoreResult>> getScoresByTeacher(@RequestBody RequestVo<String> param){
        return examService.getScoresByTeacher(param);
    }

    //  成绩区间分析时，传入的考试ID List, 返回前端渲染需要的数据
    @PostMapping("/analysisScoresByStudent")
    @ApiOperation("根据传入考试列表获取考试信息（区间分析）")
    @ResponseBody
    public ResponseVo<List<Score>> analysisScoresByStudent(@RequestBody RequestVo<List<String>> param){
        return examService.analysisScoresByStudent(param);
    }

    @PostMapping("/deleteScoreByTeacher")
    @ApiOperation("删除成绩（权限教师，根据ExamId以及UserId归集）")
    @ResponseBody
    public ResponseVo<List<Score>> deleteScoreByTeacher(@RequestBody RequestVo<Score> param){
        return examService.deleteScoreByTeacher(param);
    }

    @PostMapping("/getExamByExamId")
    @ApiOperation("根据考试KeyID获取考试信息")
    @ResponseBody
    public ResponseVo<Exam> getExamByExamId(@RequestBody RequestVo<String> param){
        return examService.getExamByExamId(param);
    }
}
