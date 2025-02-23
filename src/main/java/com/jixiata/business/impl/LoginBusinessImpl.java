package com.jixiata.business.impl;

import com.alibaba.fastjson.JSON;
import com.jixiata.business.ILoginBusiness;
import com.jixiata.dao.AuthMapper;
import com.jixiata.dao.UserMapper;
import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.LoginParam;
import com.jixiata.model.Vo.RegisterUserParam;
import com.jixiata.util.CommonUtils;
import com.jixiata.util.EncryptUtil;
import com.jixiata.util.JedisPoolWriper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Component
public class LoginBusinessImpl implements ILoginBusiness {

    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisPoolWriper wriper;

    @Value("${LOGIN_VERIFICATION_CODE_KEY}")
    private String LOGIN_VERIFICATION_CODE_KEY;

    @Override
    public boolean registerUser(RegisterUserParam param) {
        User user = param.getUser();
        Auth auth = param.getAuth();
        user.setKeyId(CommonUtils.getKeyID());
        user.setAddTime(CommonUtils.getCurrentDateString());
        user.setModifyTime(CommonUtils.getCurrentDateString());
        user.setIsDelete(0);
        auth.setKeyId(CommonUtils.getKeyID());
        auth.setSalt(EncryptUtil.getSalt());
        auth.setPassword(EncryptUtil.encode(auth.getPassword(),auth.getSalt()));
        auth.setIsLock(0);
        auth.setIsDelete(0);
        auth.setAddTime(CommonUtils.getCurrentDateString());
        auth.setModifyTime(CommonUtils.getCurrentDateString());
        user.setAuthId(auth.getKeyId());
        Integer rows = authMapper.insertAuth(auth);
        if (rows <= 0){
            throw new RuntimeException("插入权限表错误！");
        }
        Integer rows2 = userMapper.insertUser(user);
        if (rows2 <= 0){
            throw new RuntimeException("插入用户表错误！");
        }
        return true;
    }

    @Override
    public String doLogin(LoginParam param, HttpServletRequest request) {
        // 校验
        if (param.getErrorTimes() >= 111 && !isRightCheckCode(param,request)){
            return "-1";
        }
        List<Auth> auths = authMapper.getAuthByCondition(new Auth().setUsername(param.getUsername()),null,null);
        if (CollectionUtils.isEmpty(auths) || !EncryptUtil.check(param.getPassword(),auths.get(0).getPassword(),auths.get(0).getSalt())){
            return "-2";
        }
        if (auths.get(0).getIsLock() == 1){
            return "-3";
        }
        User user = userMapper.getUserInfoByAuthId(auths.get(0).getKeyId());
        if (user == null){
            return "-2";
        }
        // token生成
        String loginToken = CommonUtils.getLoginToken();
        // 存redis
        CommonUtils.storeInfoToRedis(wriper, loginToken, JSON.toJSONString(user), 900);
        return loginToken;
    }

    private boolean isRightCheckCode(LoginParam param,HttpServletRequest request) {
        String value = (String) request.getSession().getAttribute(LOGIN_VERIFICATION_CODE_KEY);
        System.out.println("获取到正确的验证码："+value);
        if (!StringUtils.isEmpty(value) && value.equals(param.getCheckCode())){
            request.getSession().setAttribute(LOGIN_VERIFICATION_CODE_KEY,"");
            return true;
        }
       return false;
    }



}
