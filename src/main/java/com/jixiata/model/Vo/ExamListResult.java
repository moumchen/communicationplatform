package com.jixiata.model.Vo;

import com.jixiata.model.Bo.Exam;
import com.jixiata.model.Bo.Score;

public class ExamListResult {
    private Exam exam;
    private Score score;

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
