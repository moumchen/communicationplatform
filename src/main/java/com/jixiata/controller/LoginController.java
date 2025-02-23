package com.jixiata.controller;

import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.LoginParam;
import com.jixiata.model.Vo.RegisterUserParam;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录与注册 Controller
 */
@Controller
@RequestMapping("/api")
@Api("用户登录与注册")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @PostMapping("/register")
    @ResponseBody
    @ApiOperation("用户注册")
    public ResponseVo<Boolean> registerAuth(@RequestBody RegisterUserParam param){
        return loginService.registerUser(param);
    }

    /**
     * 用户登录
     * 前端校验错误大于等于三次会请求验证码，以及验证码的ID
     * 用户输入验证码及登录信息、验证码ID传入进行校验
//     * @param
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    @ApiOperation("用户登录")
    public ResponseVo<String> login(String username, String password, String checkCode,Integer errorTimes, HttpServletRequest request){
        LoginParam param = new LoginParam();
        param.setUsername(username);
        param.setPassword(password);
        param.setCheckCode(checkCode);
        param.setErrorTimes(errorTimes);
        return loginService.login(param,request);
    }


}
