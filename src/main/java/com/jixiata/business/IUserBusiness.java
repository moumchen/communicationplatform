package com.jixiata.business;

import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.AllUserInfoResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;

public interface IUserBusiness {

    /**
     * 更新用户信息
     */
    Integer updateUserInfo(RequestVo<User> requestVo);

    /**
     * 更新用户缓存信息
     * @param token
     * @return
     */
    Integer updateCurrentUserInfo(String token);

    /**
     * 更新用户权限信息(管理员仅)
     * @param param
     * @return
     */
    Integer updateAuth(RequestVo<Auth> param);

    /**
     * 用户信息列表获取接口（管理员接口）
     * @param param
     * @return
     */
    ResponseVo<AllUserInfoResult> getAllUserInfo(RequestVo<Boolean> param);

    /**
     * 根据username获取用户信息
     * @param param
     * @return
     */
    ResponseVo<User> getUserInfo(RequestVo<String> param);
}
