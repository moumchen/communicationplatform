package com.jixiata.model.Vo;

import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.User;

public class CompleteUserInfoResult {
    private User user;
    private Auth auth;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }
}
