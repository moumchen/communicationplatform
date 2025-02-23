package com.jixiata.dao;

import com.jixiata.model.Bo.Score;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreMapper {

    List<Score> queryScoresByCondition(@Param("condition") Score score, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    Integer insertScore(Score score);

    Integer updateScore(Score score);

    List<Score> getScoreListByExamIds(List<String> list);
}
