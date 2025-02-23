package com.jixiata.service.impl;

import com.jixiata.business.ILoginBusiness;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.LoginParam;
import com.jixiata.model.Vo.RegisterUserParam;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.ILoginService;
import com.jixiata.util.ConstantEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private ILoginBusiness loginBusiness;

    @Override
    public ResponseVo<Boolean> registerUser(RegisterUserParam param) {
        ResponseVo<Boolean> responseVo = new ResponseVo<>();
        // 参数校验
        if (!checkRegisterParam(param)){
            responseVo.setData(false);
            responseVo.setMessage("参数错误！");
            responseVo.setSuccess(false);
            return responseVo;
        }
        try {
            boolean isSuccess = loginBusiness.registerUser(param);
            if (isSuccess){
                responseVo.setMessage("注册成功！");
            } else {
                responseVo.setMessage("注册失败！");
            }
            responseVo.setData(isSuccess);
            responseVo.setSuccess(isSuccess);
            responseVo.setStatusCode(ConstantEnum.SUCCESS.getStatusCode());
        } catch (Exception e){
            e.printStackTrace();
            responseVo.setMessage("注册用户发生错误："+e.getMessage());
            responseVo.setSuccess(false);
            responseVo.setData(false);
            responseVo.setStatusCode(ConstantEnum.FAIL.getStatusCode());
        }
        return responseVo;
    }

    @Override
    public ResponseVo<String> login(LoginParam param, HttpServletRequest request) {
        ResponseVo<String> responseVo = new ResponseVo<>();
        if (param == null || param.getErrorTimes() == null || StringUtils.isEmpty(param.getUsername()) || StringUtils.isEmpty(param.getPassword())
                || (param.getErrorTimes() != null && param.getErrorTimes() > 111 && StringUtils.isEmpty(param.getCheckCode()))){
            responseVo.setMessage("入参有误");
            responseVo.setSuccess(false);
            responseVo.setData(null);
            return responseVo;
        }
        try {
            String token = loginBusiness.doLogin(param, request);
            if ("-1".equals(token)){
                responseVo.setMessage("验证码错误");
                responseVo.setSuccess(false);
                responseVo.setStatusCode(ConstantEnum.FAIL.getStatusCode());
            } else if ("-2".equals(token)){
                responseVo.setMessage("用户名或者密码错误");
                responseVo.setSuccess(false);
                responseVo.setStatusCode(ConstantEnum.FAIL.getStatusCode());
            } else if ("-3".equals(token)){
                responseVo.setMessage("您的账户已被锁定！有异议请联系管理员！admin@jixiata.com");
                responseVo.setSuccess(false);
                responseVo.setStatusCode(ConstantEnum.FAIL.getStatusCode());
            } else {
                responseVo.setMessage("登录成功");
                responseVo.setSuccess(true);
                responseVo.setData(token);
                responseVo.setStatusCode(ConstantEnum.SUCCESS.getStatusCode());
            }
        } catch (Exception e){
            e.printStackTrace();
            responseVo.setSuccess(false);
            responseVo.setMessage("登录异常:"+e.getMessage());
        }
        return responseVo;
    }

    /**
     * 参数校验 注册
     * @param param
     * @return
     */
    private boolean checkRegisterParam(RegisterUserParam param) {
        boolean result = (param == null || param.getAuth() == null || param.getUser() == null ||
        StringUtils.isEmpty(param.getUser().getNickname()) || param.getUser().getIdentity() == null ||
        StringUtils.isEmpty(param.getAuth().getUsername()) || StringUtils.isEmpty(param.getAuth().getPassword())) ? false : true;
        return result;
    }
}
