package com.jixiata.dao;

import com.jixiata.model.Bo.Class;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassMapper {

    /**
     * 班级新建
     * @param data
     * @return
     */
    Integer insertClass(Class data);

    /**
     * 根据Condition获取班级信息
     * @param aClass
     * @return
     */
    List<Class> getClassInfoByCondition(Class aClass);

    /**
     * 根据KeyID更新班级信息
     * @param data
     * @return
     */
    Integer updateClassInfoByKeyId(Class data);
}
