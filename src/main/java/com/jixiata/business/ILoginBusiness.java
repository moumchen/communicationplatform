package com.jixiata.business;

import com.jixiata.model.Vo.LoginParam;
import com.jixiata.model.Vo.RegisterUserParam;

import javax.servlet.http.HttpServletRequest;

public interface ILoginBusiness {

    /**
     * 注册用户
     * @param param
     * @return
     */
    public boolean registerUser(RegisterUserParam param);

    /**
     * 登录用户（生成TOKEN,存REDIS等操作）
     * @param param
     * @return
     */
    public String doLogin(LoginParam param, HttpServletRequest request);
}
