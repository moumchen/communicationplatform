package com.jixiata.service;

import com.jixiata.model.Vo.LoginParam;
import com.jixiata.model.Vo.RegisterUserParam;
import com.jixiata.model.Vo.ResponseVo;

import javax.servlet.http.HttpServletRequest;

public interface ILoginService {

    /**
     * 注册用户
     * @param param
     * @return
     */
    public ResponseVo<Boolean> registerUser(RegisterUserParam param);

    /**
     * 用户登录
     * @param param
     * @return
     */
    public ResponseVo<String> login(LoginParam param, HttpServletRequest request);
}
