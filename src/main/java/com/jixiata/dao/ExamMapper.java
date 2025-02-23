package com.jixiata.dao;

import com.jixiata.model.Bo.Exam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamMapper {
    /**
     * 插入考试
     * @param exam
     * @return
     */
    Integer insertExam(Exam exam);

    /**
     * 根据Condition获取考试信息
     * @param condition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<Exam> getExamInfoByCondition(@Param("condition") Exam condition, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    /**
     * 根据Condtion获取考试信息数量
     * @param conditon
     * @return
     */
    Integer getExamInfoCountByCondition(Exam conditon);

    /**
     * 根据KeyId更新考试信息
     * @param exam
     * @return
     */
    Integer updateExamByExamId(Exam exam);


}
