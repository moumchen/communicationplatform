package com.jixiata.interceptor;

import com.alibaba.fastjson.JSON;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.util.BeanUtils;
import com.jixiata.util.JedisPoolWriper;
import com.jixiata.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 权限拦截器 教师用户
 */
public class TeacherOperationPermissionInterceptor extends HandlerInterceptorAdapter{

    @Autowired
    private JedisPoolWriper wriper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(">>>>> 教师权限拦截器 <<<<<");
        ServletInputStream inputStream = request.getInputStream();
        ServletInputStream tempInputStream = (ServletInputStream) org.apache.commons.beanutils.BeanUtils.cloneBean(inputStream);
        byte[] readbyte = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while (tempInputStream.read(readbyte) != -1){
            sb.append(new String(readbyte));
        }
        // 将输入流置为最初
        RequestVo requestVo = JSON.parseObject(sb.toString(), RequestVo.class);
        if (requestVo == null || StringUtils.isEmpty(requestVo.getToken())){
            return false;
        }
        User user = UserUtils.getCurrentUserByToken(wriper, requestVo.getToken());
        if (user == null || user.getIdentity() != 1){
            return false;
        }
        return true;
    }
}
