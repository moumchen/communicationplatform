package com.jixiata.business;

import com.jixiata.model.Bo.Exam;
import com.jixiata.model.Bo.Score;
import com.jixiata.model.Vo.ExamListResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.model.Vo.StudentAndScoreResult;

import java.util.List;

public interface IExamBusiness {

    /**
     * 新建班级 权限教师
     * @param param
     * @return
     */
    ResponseVo<Exam> addExamByTeacher(RequestVo<Exam> param);

    /**
     * 批量录入成绩信息
     * @param param
     * @return
     */
    ResponseVo<Boolean> addScoresByTeacher(RequestVo<List<Score>> param);

    /**
     * 更新考试信息
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateExamInfoByTeacher(RequestVo<Exam> param);

    /**
     * 批量修改成绩信息
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateScoresByTeacher(RequestVo<List<Score>> param);

    /**
     * 获取当前用户所在班级的考试信息
     * @param param
     * @return
     */
    ResponseVo<List<ExamListResult>> getExamList(RequestVo param);

    /**
     * 学生用户获取某门考试的成绩
     * @param param
     * @return
     */
    ResponseVo<StudentAndScoreResult> getScoreByStudent(RequestVo<String> param);

    /**
     * 教师用户获取某门考试所有学生的成绩
     * @param param
     * @return
     */
    ResponseVo<List<StudentAndScoreResult>> getScoresByTeacher(RequestVo<String> param);

    /**
     * 区间分析数据获取
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
     * 获取考试信息
     * @param param
     * @return
     */
    ResponseVo<Exam> getExamByExamId(RequestVo<String> param);
}
