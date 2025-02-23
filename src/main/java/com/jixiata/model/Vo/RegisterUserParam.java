package com.jixiata.model.Vo;

import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.User;

/**
 * 注册请求的入参
 */
public class RegisterUserParam {
    private Auth auth;
    private User user;

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
