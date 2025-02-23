package com.jixiata.dao;

import com.jixiata.model.Bo.Auth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthMapper {


    /**
     * 插入认证表数据
     * @param auth
     * @return
     */
     Integer insertAuth(Auth auth);

    /**
     * 通过用户名获取权限信息
     * @param auth
     * @return
     */
     List<Auth> getAuthByCondition(@Param("condition") Auth auth,@Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    /**
     * 通过AuthId更新权限信息
     * @param data
     * @return
     */
    Integer updateAuthByAuthId(Auth data);

    /**
     * 根据Condition获取数量
     * @param auth
     * @return
     */
    Integer getCountByCondition(Auth auth);
}
