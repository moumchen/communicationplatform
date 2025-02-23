package com.jixiata.service.impl;

import com.jixiata.business.IUserBusiness;
import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.AllUserInfoResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IUserService;
import com.jixiata.util.CommonUtils;
import com.jixiata.util.ConstantEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserBusiness userBusiness;

    @Override
    public ResponseVo<Boolean> updateUserInfo(RequestVo<User> requestVo) {
        ResponseVo<Boolean> responseVo = new ResponseVo<>();
        if (requestVo == null || requestVo.getData() == null || StringUtils.isEmpty(requestVo.getToken())){
            responseVo.setSuccess(false);
            responseVo.setMessage("入参错误");
            return responseVo;
        }
        try {
            Integer result = userBusiness.updateUserInfo(requestVo);
            if (result == ConstantEnum.PERMISSION_DENIED.getStatusCode()){
                return ResponseVo.getPermissonDeniedResponseVo();
            }
            responseVo = ResponseVo.getSuccessResponseVo();
        } catch (Exception e) {
            e.printStackTrace();
            responseVo = ResponseVo.getResponseVo(false, false, ConstantEnum.FAIL.getStatusCode(), "用户信息维护异常:"+e.getMessage());
        }
        return responseVo;

    }

    @Override
    public ResponseVo<Boolean> updateCurrentUserInfo(String token) {
        ResponseVo<Boolean> responseVo = new ResponseVo<>();
        if (StringUtils.isEmpty(token)){
            return ResponseVo.getResponseVo(false,false,ConstantEnum.CHECK_FAIL.getStatusCode(), "入参错误");
        }
        try {
            Integer result = userBusiness.updateCurrentUserInfo(token);
            if (result == ConstantEnum.PERMISSION_DENIED.getStatusCode()){
                return ResponseVo.getPermissonDeniedResponseVo();
            }
            responseVo = ResponseVo.getSuccessResponseVo();
        } catch (Exception e){
            e.printStackTrace();
            responseVo = ResponseVo.getResponseVo(false, false, ConstantEnum.FAIL.getStatusCode(),"更新用户信息缓存出错:"+e.getMessage());
        }
        return responseVo;
    }

    @Override
    public ResponseVo<Boolean> updateAuth(RequestVo<Auth> param) {
        ResponseVo<Boolean> responseVo = new ResponseVo<>();
        if (param == null || param.getData() == null || StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            Integer result = userBusiness.updateAuth(param);
            if (result == ConstantEnum.PERMISSION_DENIED.getStatusCode()){
                responseVo = ResponseVo.getPermissonDeniedResponseVo();
            } else if (result == ConstantEnum.FAIL.getStatusCode()){
                responseVo = ResponseVo.getFailResponseVo();
            } else {
                responseVo = ResponseVo.getSuccessResponseVo();
            }
        } catch (Exception e){
            e.printStackTrace();
            responseVo = ResponseVo.getResponseVo(false, false, ConstantEnum.FAIL.getStatusCode(),"更新用户信息缓存出错:"+e.getMessage());
        }
        return responseVo;
    }

    @Override
    public ResponseVo<AllUserInfoResult> getAllUserInfo(RequestVo<Boolean> param) {
        ResponseVo<AllUserInfoResult> responseVo = new ResponseVo<>();
        if (param == null || StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            responseVo = userBusiness.getAllUserInfo(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getResponseVo(false,null,ConstantEnum.FAIL.getStatusCode(),"查询用户信息错误:"+e.getMessage());
        }
        return responseVo;
    }

    @Override
    public ResponseVo<User> getUserInfo(RequestVo<String> param) {
        if (param == null || StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try{
            return userBusiness.getUserInfo(param);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取当前用户信息错误:"+e.getMessage());
        }
    }

}
