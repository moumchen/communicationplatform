package com.jixiata.service;

import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.AllUserInfoResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;

import java.util.List;

public interface IUserService {

    /**
     * 用户信息维护
     * @param requestVo
     * @return
     */
    public ResponseVo<Boolean> updateUserInfo(RequestVo<User> requestVo);

    /**
     * 更新当前用户缓存信息
     * @param token
     * @return
     */
    ResponseVo<Boolean> updateCurrentUserInfo(String token);

    /**
     * 更新用户权限表信息（管理员接口）
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateAuth(RequestVo<Auth> param);
    /**
     * 分页获取用户信息（管理员接口）
     */
    ResponseVo<AllUserInfoResult> getAllUserInfo(RequestVo<Boolean> param);

    /**
     * 根据username获取用户信息
     *
     * @param param
     * @return
     */
    ResponseVo<User> getUserInfo(RequestVo<String> param);
}
