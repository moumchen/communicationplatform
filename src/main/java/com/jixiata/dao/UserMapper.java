package com.jixiata.dao;

import com.jixiata.model.Bo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    /**
     * 插入用户
     */
    public Integer insertUser(User user);

    /**
     * 通过权限ID获取用户信息
     * @param authId
     * @return
     */
    User getUserInfoByAuthId(@Param("authId") String authId);

    /**
     * 通过KeyId获取用户信息
     */
    User getUserInfoByKeyId(@Param("keyId") String keyId);

    /**
     * 通过用户id更新用户信息
     */
    Integer updateUserInfoByKeyId(User user);

    /**
     * 根据Condition 获取用户信息集合
     * @param user
     * @return
     */
    List<User> getUserInfoByCondition(@Param("condition") User user, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

}
