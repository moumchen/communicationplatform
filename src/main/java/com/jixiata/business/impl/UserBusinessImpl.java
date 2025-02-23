package com.jixiata.business.impl;

import com.alibaba.fastjson.JSON;
import com.jixiata.business.IUserBusiness;
import com.jixiata.dao.AuthMapper;
import com.jixiata.dao.UserMapper;
import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.AllUserInfoResult;
import com.jixiata.model.Vo.CompleteUserInfoResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.util.CommonUtils;
import com.jixiata.util.ConstantEnum;
import com.jixiata.util.JedisPoolWriper;
import com.jixiata.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class UserBusinessImpl implements IUserBusiness {

    @Autowired
    private JedisPoolWriper wriper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthMapper authMapper;

    @Override
    public Integer updateUserInfo(RequestVo<User> requestVo) {
        // 验证
        User user = UserUtils.getCurrentUserByToken(wriper, requestVo.getToken());
        if (user == null){
            return ConstantEnum.PERMISSION_DENIED.getStatusCode();
        }
        User modifyUser = requestVo.getData();
        modifyUser.setKeyId(user.getKeyId());
        Integer rows = userMapper.updateUserInfoByKeyId(modifyUser);
        this.updateCurrentUserInfo(requestVo.getToken());
        return ConstantEnum.SUCCESS.getStatusCode();
    }

    @Override
    public Integer updateCurrentUserInfo(String token) {
        String json = CommonUtils.getInfoFromRedis(wriper, token);
        if (StringUtils.isEmpty(json)){
            return ConstantEnum.PERMISSION_DENIED.getStatusCode();
        }
        User user = JSON.parseObject(json, User.class);
        User newestUser = userMapper.getUserInfoByKeyId(user.getKeyId());
        CommonUtils.storeInfoToRedis(wriper,token,JSON.toJSONString(newestUser),900);
        System.out.println("重新更新用户信息:"+JSON.toJSONString(newestUser));
        return ConstantEnum.SUCCESS.getStatusCode();
    }

    @Override
    public Integer updateAuth(RequestVo<Auth> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 2){
            return ConstantEnum.PERMISSION_DENIED.getStatusCode();
        }
        Integer rows = authMapper.updateAuthByAuthId(param.getData());
        return ConstantEnum.SUCCESS.getStatusCode();
    }

    @Override
    public ResponseVo<AllUserInfoResult> getAllUserInfo(RequestVo<Boolean> param) {
        AllUserInfoResult result = new AllUserInfoResult();
        List<CompleteUserInfoResult> list = new ArrayList<>();
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null || user.getIdentity() != 2){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        param.setPageIndex(param.getPageIndex() == null? 1 : param.getPageIndex());
        param.setPageSize(param.getPageSize() == null ? 20 : param.getPageSize());
        // 获取用户总数
        Integer count = authMapper.getCountByCondition(new Auth());
        result.setTotalCount(count);
        if (count != 0){
            List<Auth> auths = authMapper.getAuthByCondition(new Auth(), param.getPageIndex(), param.getPageSize());
            for (Auth auth : auths){
                CompleteUserInfoResult infoResult = new CompleteUserInfoResult();
                User userinfo = userMapper.getUserInfoByAuthId(auth.getKeyId());
                auth.setPassword("******");
                infoResult.setAuth(auth);
                infoResult.setUser(userinfo);
                list.add(infoResult);
            }
        }
        result.setResult(list);
        return ResponseVo.getResponseVo(true,result,ConstantEnum.SUCCESS.getStatusCode(),"查询成功");
    }

    @Override
    public ResponseVo<User> getUserInfo(RequestVo<String> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        User u = userMapper.getUserInfoByKeyId(user.getKeyId());
        return ResponseVo.getResponseVo(true, u, ConstantEnum.SUCCESS.getStatusCode(), "查询成功");
    }

}
