package com.jixiata.dao;

import com.jixiata.model.Bo.IndexInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexInfoMapper {


    /**
     * 插入数据
     * @param indexInfo
     * @return
     */
    Integer insertIndexInfo(IndexInfo indexInfo);

    /**
     * 通过用户名获取信息
     * @param condition
     * * @return
     */
    List<IndexInfo> getIndexInfoByCondition(@Param("condition") IndexInfo condition, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    /**
     * 通过IndexInfoId更新信息
     * @param data
     * @return
     */
    Integer updateIndexInfoByKeyId(IndexInfo data);

    /**
     * 根据Condition获取数量
     * @param indexInfo
     * @return
     */
    Integer getCountByCondition(IndexInfo indexInfo);
}
