package com.jixiata.business.impl;

import com.alibaba.fastjson.JSON;
import com.jixiata.business.IExamBusiness;
import com.jixiata.dao.ExamMapper;
import com.jixiata.dao.ScoreMapper;
import com.jixiata.dao.UserMapper;
import com.jixiata.model.Bo.Exam;
import com.jixiata.model.Bo.Score;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.ExamListResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.model.Vo.StudentAndScoreResult;
import com.jixiata.util.CommonUtils;
import com.jixiata.util.ConstantEnum;
import com.jixiata.util.JedisPoolWriper;
import com.jixiata.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
@SuppressWarnings("unchecked")
public class ExamBusinessImpl implements IExamBusiness {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private JedisPoolWriper wriper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseVo<Exam> addExamByTeacher(RequestVo<Exam> param) {
        // 权限校验 : 教师
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 1){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        Exam exam = param.getData();
        exam.setKeyId(CommonUtils.getKeyID());
        exam.setClassId(user.getClassId());
        exam.setAddTime(CommonUtils.getCurrentDateString());
        exam.setAverageScore(0);
        exam.setMaxScore(0);
        exam.setIsDelete(0);
        exam.setNum(0);
        exam.setModifyTime(CommonUtils.getCurrentDateString());
        int rows = examMapper.insertExam(exam);
        if (rows == 0){
            throw new RuntimeException("新增考试错误");
        }
        return ResponseVo.getResponseVo(true,exam, ConstantEnum.SUCCESS.getStatusCode(), "操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<Boolean> addScoresByTeacher(RequestVo<List<Score>> param) {
        // 权限校验 : 教师
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 1){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        // 新添数据
        List<Score> scores = param.getData();
        // 获取该考试的原本数据
        String examId = scores.get(0).getExamId();
        Exam examCondition = new Exam();
        examCondition.setKeyId(examId);
        List<Exam> examInfos = examMapper.getExamInfoByCondition(examCondition, 1, 1);
        Exam examInfo = examInfos.get(0);
        List<Score> oldScores = getScoresByExamId(examId);

        // 记录原来的成绩排名信息
        Map<String,Integer> oldRank = new HashMap<>();
        for (Score s : oldScores){
            oldRank.put(s.getKeyId(), s.getRank());
        }
        // 排序
        int totalScores = 0;
        int rank = 0;
        oldScores.addAll(scores);
        sort(oldScores);

        for (Score s : oldScores){
            s.setRank(++rank);
            totalScores += s.getScore();
           if (StringUtils.isEmpty(s.getKeyId())){
               s.setAddTime(CommonUtils.getCurrentDateString());
               s.setModifyTime(CommonUtils.getCurrentDateString());
               s.setKeyId(CommonUtils.getKeyID());
               s.setIsDelete(0);
               Integer rows = scoreMapper.insertScore(s);
               if (rows == 0){
                   throw new RuntimeException("插入错误："+s.getUserId()+" "+s.getScore());
               }
           } else {
               // 判断是否更新数据
               if (oldRank.get(s.getKeyId()) != s.getRank()){
                   s.setModifyTime(CommonUtils.getCurrentDateString());
                   Integer row = scoreMapper.updateScore(s);
                   if (row == 0){
                       throw new RuntimeException("更新错误:"+s.getKeyId()+"(即将更新数据:"+ JSON.toJSON(s)+")");
                   }
               }
           }
        }
        examInfo.setMaxScore(oldScores.get(0).getScore());
        examInfo.setAverageScore(totalScores/oldScores.size());
        examInfo.setNum(oldScores.size());
        // 根据插入的数据对考试信息进行更新
        Integer rows = examMapper.updateExamByExamId(examInfo);
        if (rows == 0){
            throw new RuntimeException("更新考试信息错误！");
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<Boolean> updateExamInfoByTeacher(RequestVo<Exam> param) {
        // 权限校验
        // 权限校验 : 教师
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 1){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        Exam data = param.getData();
        data.setModifyTime(CommonUtils.getCurrentDateString());
        Integer rows = examMapper.updateExamByExamId(data);
        if (rows == 0){
            throw new RuntimeException("未更新到考试表信息，请检查主键是否正确:"+data.getKeyId());
        }
        return ResponseVo.getSuccessResponseVo();
    }

    /**
     * 根据考试ID查询现有的成绩信息
     * @param examId
     * @return
     */
    private List<Score> getScoresByExamId(String examId){
        Score scoreCondition = new Score();
        scoreCondition.setExamId(examId);
        List<Score> oldScores = new ArrayList<>();
        int pageIndex = 1;
        List<Score> tempScore;
        do {
            tempScore = scoreMapper.queryScoresByCondition(scoreCondition, pageIndex, 50);
            oldScores.addAll(tempScore);
            pageIndex ++;
        } while (tempScore.size() == 50);
        return oldScores;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<Boolean> updateScoresByTeacher(RequestVo<List<Score>> param) {
        // 权限校验 : 教师
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 1){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        List<Score> scores = param.getData();
        // 获取考试信息
        Exam exam = new Exam();
        exam.setKeyId(scores.get(0).getExamId());
        List<Exam> examInfo = examMapper.getExamInfoByCondition(exam, 1, 1);
        // 获取该考试的原本数据
        List<Score> oldScores = getScoresByExamId(scores.get(0).getExamId());

        for (Score s : oldScores){
            for (Score temp : scores){
                if (temp.getUserId().equals(s.getUserId()) && temp.getExamId().equals(s.getExamId())){
                    s.setScore(temp.getScore());
                }
            }
        }

        // 排序
        sort(oldScores);
        int rank = 0;
        int totalScores = 0;

        for (Score s : oldScores){
            s.setRank(++rank);
            totalScores += s.getScore();
            s.setModifyTime(CommonUtils.getCurrentDateString());
            Integer row = scoreMapper.updateScore(s);
            if (row == 0){
                throw new RuntimeException("更新成绩异常:"+s.getKeyId());
            }
        }
        examInfo.get(0).setAverageScore(totalScores/oldScores.size());
        examInfo.get(0).setMaxScore(oldScores.get(0).getScore());
        Integer row = examMapper.updateExamByExamId(examInfo.get(0));
        if (row == 0){
            throw new RuntimeException("更新考试信息异常");
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<List<ExamListResult>> getExamList(RequestVo param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || StringUtils.isEmpty(user.getClassId())){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        param.setPageIndex(param.getPageIndex() == null ? 1 : param.getPageIndex());
        param.setPageSize(param.getPageSize() == null ? 10 : param.getPageSize());

        // 获取当前用户所在班级的考试信息及总数
        String classid = user.getClassId();
        Exam condition = new Exam();
        condition.setClassId(classid);
        Integer count = examMapper.getExamInfoCountByCondition(condition);
        List<Exam> exams  = examMapper.getExamInfoByCondition(condition, param.getPageIndex(), param.getPageSize());

        List<ExamListResult> result = new ArrayList<>();
            for (Exam e : exams) {
                ExamListResult elr = new ExamListResult();
                elr.setExam(e);
                // 学生用户查询一波成绩
                if (user.getIdentity() == 0){
                    Score scoreCondition = new Score();
                    scoreCondition.setUserId(user.getKeyId());
                    scoreCondition.setExamId(e.getKeyId());
                    List<Score> scores = scoreMapper.queryScoresByCondition(scoreCondition, 1, 1);
                    if (scores.size() > 0){
                        elr.setScore(scores.get(0));
                    }
                }
                result.add(elr);
            }

        return ResponseVo.getResponseVo(true,result, ConstantEnum.SUCCESS.getStatusCode(), "操作成功", count);
    }

    @Override
    public ResponseVo<StudentAndScoreResult> getScoreByStudent(RequestVo<String> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 0){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        String examId = param.getData();
        String userId = user.getKeyId();
        Score condition = new Score();
        condition.setExamId(examId);
        condition.setUserId(userId);
        List<Score> scores = scoreMapper.queryScoresByCondition(condition, 1, 1);

        StudentAndScoreResult result = new StudentAndScoreResult();
        result.setStudent(user);
        result.setScore(scores != null && scores.size() == 0 ? null : scores.get(0));
        return ResponseVo.getResponseVo(true, result, ConstantEnum.SUCCESS.getStatusCode(), "查询成功");
    }

    @Override
    public ResponseVo<List<StudentAndScoreResult>> getScoresByTeacher(RequestVo<String> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 1){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        String examId = param.getData();
        Score condition = new Score();
        condition.setExamId(examId);
        List<Score> temp;
        List<Score> scores = new ArrayList<>();
        int pageIndex = 1;
        do {
            temp = scoreMapper.queryScoresByCondition(condition, pageIndex, 50);
            scores.addAll(temp);
            pageIndex++;
        } while (temp.size() == 50);

        List<StudentAndScoreResult> result = new ArrayList<>();
        for (Score score : scores){
            StudentAndScoreResult r = new StudentAndScoreResult();
            String userId = score.getUserId();
            User student = userMapper.getUserInfoByKeyId(userId);
            r.setScore(score);
            r.setStudent(student);
            result.add(r);
        }

        return ResponseVo.getResponseVo(true, result, ConstantEnum.SUCCESS.getStatusCode(), "查询成功");
    }

    @Override
    public ResponseVo<List<Score>> analysisScoresByStudent(RequestVo<List<String>> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 0){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        List<Score> scores = scoreMapper.getScoreListByExamIds(param.getData());
        return ResponseVo.getResponseVo(true,scores,ConstantEnum.SUCCESS.getStatusCode(),"查询成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<List<Score>> deleteScoreByTeacher(RequestVo<Score> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 1){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        Score score = param.getData();
        score.setIsDelete(1);
        // 保存本次删除的
        Integer row = scoreMapper.updateScore(score);
        if (row <= 0){
            throw new RuntimeException("删除成绩错误!");
        }
        Exam exam = new Exam();
        exam.setKeyId(score.getExamId());
        List<Exam> examInfos = examMapper.getExamInfoByCondition(exam, 1, 1);
        if (examInfos == null || examInfos.size() == 0){
            throw new RuntimeException("未获取到考试信息");
        }
        Exam examInfo  = examInfos.get(0);
        // 再次获取成绩进行归集
        List<Score> oldScores = getScoresByExamId(score.getExamId());
        // 排序
        sort(oldScores);
        int rank = 0;
        int totalScores = 0;

        for (Score s : oldScores){
            s.setRank(++rank);
            totalScores += s.getScore();
            s.setModifyTime(CommonUtils.getCurrentDateString());
            Integer row2 = scoreMapper.updateScore(s);
            if (row2 == 0){
                throw new RuntimeException("更新成绩异常:"+s.getKeyId());
            }
        }
        if (oldScores.size() != 0) {
            examInfo.setAverageScore(totalScores / oldScores.size());
            examInfo.setMaxScore(oldScores.get(0).getScore());
        } else {
            examInfo.setAverageScore(0);
            examInfo.setMaxScore(0);
        }
        examInfo.setNum(examInfo.getNum() - 1);
        examInfo.setModifyTime(CommonUtils.getCurrentDateString());
        Integer row3 = examMapper.updateExamByExamId(examInfo);
        if (row3 == 0){
            throw new RuntimeException("更新考试信息异常");
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<Exam> getExamByExamId(RequestVo<String> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null ){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        Exam condition = new Exam();
        condition.setKeyId(param.getData());
        List<Exam> examInfoByCondition = examMapper.getExamInfoByCondition(condition, 1, 1);
        return ResponseVo.getResponseVo(true,examInfoByCondition.get(0), ConstantEnum.SUCCESS.getStatusCode(),"获取成功!");
    }

    private void sort(List<Score> scores){
        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return o2.getScore() - o1.getScore();
            }
        });
    }
}
