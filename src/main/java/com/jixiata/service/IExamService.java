package com.jixiata.service;

import com.jixiata.model.Bo.Exam;
import com.jixiata.model.Bo.Score;
import com.jixiata.model.Vo.ExamListResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.model.Vo.StudentAndScoreResult;

import java.util.List;

public interface IExamService {

    /**
     * 新增考试 （权限：教师）
     * @param param
     * @return
     */
    ResponseVo<Exam> addExamByTeacher(RequestVo<Exam> param);

    /**
     * 批量添加成绩 （权限：教师）
     * @param param
     * @return
     */
    ResponseVo<Boolean> addScoresByTeacher(RequestVo<List<Score>> param);

    /**
     * 更新考试信息 （权限： 教师）
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateExamInfoByTeacher(RequestVo<Exam> param);

    /**
     * 批量更新成绩信息 （权限：教师）
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateScoresByTeacher(RequestVo<List<Score>> param);

    /**
     * 获取当前用户所在班级考试列表
     * @param param
     * @return
     */
    ResponseVo<List<ExamListResult>> getExamList(RequestVo param);

    /**
     * 根据考试ID获取当前用户成绩（学生）
     * @param param
     * @return
     */
    ResponseVo<StudentAndScoreResult> getScoreByStudent(RequestVo<String> param);

    /**
     * 根据考试ID获取所有学生成绩列表（教师）
     * @param param
     * @return
     */
    ResponseVo<List<StudentAndScoreResult>> getScoresByTeacher(RequestVo<String> param);

    /**
     * 根据传入考试列表获取考试信息（区间分析）
     * @param param
     * @return
     */
    ResponseVo<List<Score>> analysisScoresByStudent(RequestVo<List<String>> param);

    /**
     * 删除成绩
     * @param param
     * @return
     */
    ResponseVo<List<Score>> deleteScoreByTeacher(RequestVo<Score> param);

    /**
     * 根据考试ID获取考试信息
     * @param param
     * @return
     */
    ResponseVo<Exam> getExamByExamId(RequestVo<String> param);
}
