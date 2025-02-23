package com.jixiata.service.impl;

import com.jixiata.business.IExamBusiness;
import com.jixiata.model.Bo.Exam;
import com.jixiata.model.Bo.Score;
import com.jixiata.model.Vo.ExamListResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.model.Vo.StudentAndScoreResult;
import com.jixiata.service.IExamService;
import com.jixiata.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class ExamServiceImpl implements IExamService {

    @Autowired
    private IExamBusiness examBusiness;


    @Override
    public ResponseVo<Exam> addExamByTeacher(RequestVo<Exam> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        Exam exam = param.getData();
        if (StringUtils.isEmpty(exam.getExamName())){
            return ResponseVo.getParamErrorAndInfoResponseVo("考试名称为空");
        }
        if (!CommonUtils.checkDateString(exam.getStartTime()) || !CommonUtils.checkDateString(exam.getEndTime())){
            return ResponseVo.getParamErrorAndInfoResponseVo("考试开始时间或结束时间有误");
        }
        try {
            return examBusiness.addExamByTeacher(param);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("新建班级异常："+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> addScoresByTeacher(RequestVo<List<Score>> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        for (Score sc : param.getData()){
            if (StringUtils.isEmpty(sc.getUserId()) || sc.getScore() == null){
                return ResponseVo.getFailResponseVoByMessage("存在录入的成绩没有学生ID或成绩！");
            }
        }
        try {
            return examBusiness.addScoresByTeacher(param);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("批量录入成绩异常："+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> updateExamInfoByTeacher(RequestVo<Exam> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        Exam exam = param.getData();
        if (StringUtils.isEmpty(exam.getKeyId())){
            return ResponseVo.getFailResponseVoByMessage("KeyId 为空");
        }
        try {
            return examBusiness.updateExamInfoByTeacher(param);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("考试信息更新异常："+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> updateScoresByTeacher(RequestVo<List<Score>> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        for (Score sc : param.getData()){
            if (StringUtils.isEmpty(sc.getUserId()) || sc.getScore() == null){
                return ResponseVo.getFailResponseVoByMessage("存在录入的成绩没有学生ID或成绩！");
            }
        }
        try {
            return examBusiness.updateScoresByTeacher(param);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("批量录入成绩异常："+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<ExamListResult>> getExamList(RequestVo param) {
        if (param == null || StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return examBusiness.getExamList(param);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取当前用户的考试信息错误："+e.getMessage());
        }
    }

    @Override
    public ResponseVo<StudentAndScoreResult> getScoreByStudent(RequestVo<String> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return examBusiness.getScoreByStudent(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("学生用户获取成绩信息错误"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<StudentAndScoreResult>> getScoresByTeacher(RequestVo<String> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return examBusiness.getScoresByTeacher(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("教师用户获取成绩信息错误"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<Score>> analysisScoresByStudent(RequestVo<List<String>> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        if (param.getData().size() <= 1){
            return ResponseVo.getParamErrorAndInfoResponseVo("考试ID不能为空或不能少于两个");
        }
        try {
            return examBusiness.analysisScoresByStudent(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("区间分析获取数据错误："+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<Score>> deleteScoreByTeacher(RequestVo<Score> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return examBusiness.deleteScoreByTeacher(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("删除成绩错误："+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Exam> getExamByExamId(RequestVo<String> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return examBusiness.getExamByExamId(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取考试信息错误："+e.getMessage());
        }
    }
}
