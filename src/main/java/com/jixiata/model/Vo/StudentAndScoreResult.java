package com.jixiata.model.Vo;

import com.jixiata.model.Bo.Score;
import com.jixiata.model.Bo.User;

public class StudentAndScoreResult {
    /**
     * 用户信息
     */
    private User student;
    /**
     * 成绩信息
     */
    private Score score;

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
