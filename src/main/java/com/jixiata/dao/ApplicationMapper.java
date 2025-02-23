package com.jixiata.dao;

import com.jixiata.model.Bo.Application;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationMapper {


    /**
     * 插入数据
     * @param application
     * @return
     */
    Integer insertApplication(Application application);

    /**
     * 通过用户名获取信息
     * @param condition
     * * @return
     */
    List<Application> getApplicationByCondition(@Param("condition") Application condition,@Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    /**
     * 通过ApplicationId更新信息
     * @param data
     * @return
     */
    Integer updateApplicationByKeyId(Application data);

    /**
     * 根据Condition获取数量
     * @param Application
     * @return
     */
    Integer getCountByCondition(Application Application);
}
