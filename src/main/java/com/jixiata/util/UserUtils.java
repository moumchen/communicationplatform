package com.jixiata.util;

import com.alibaba.fastjson.JSON;
import com.jixiata.dao.UserMapper;
import com.jixiata.model.Bo.User;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {

    /**
     * 根据请求TOKEN获取用户信息
     * @param wriper
     * @param token
     * @return
     */
    public static User getCurrentUserByToken(JedisPoolWriper wriper, String token){
        String json = CommonUtils.getInfoFromRedis(wriper, token);
        if (StringUtils.isEmpty(json)){
            return null;
        }
        return JSON.parseObject(json, User.class);
    }

    /**
     *  临时 ： 更新当前Redis用户信息
     */
    public static void updateCurrentUserInfo(String token, User oldUser, UserMapper userMapper,JedisPoolWriper wriper){
        User newUser = userMapper.getUserInfoByKeyId(oldUser.getKeyId());
        wriper.getJedisPool().getResource().set(token, JSON.toJSONString(newUser));
    }

}
