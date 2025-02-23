package com.jixiata.service.impl;

import com.jixiata.business.IApplicationBusiness;
import com.jixiata.model.Bo.Application;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class ApplicationServiceImpl implements IApplicationService {

    @Autowired
    private IApplicationBusiness applicationBusiness;

    @Override
    public ResponseVo<Boolean> insertApplcationByStudent(RequestVo<Application> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return applicationBusiness.insertApplcationByStudent(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("新增申请错误："+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<Application>> getApplication(String param) {
        if (param == null || StringUtils.isEmpty(param)){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return applicationBusiness.getApplication(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("查询申请错误："+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> updateApplication(RequestVo<Application> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return applicationBusiness.updateApplication(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("更新申请错误："+e.getMessage());
        }
    }
}
