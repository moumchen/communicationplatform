package com.jixiata.service.impl;

import com.jixiata.business.IClassBusiness;
import com.jixiata.model.Bo.Class;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IClassService;
import com.jixiata.util.ConstantEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class ClassServiceImpl implements IClassService {

    @Autowired
    private IClassBusiness classBusiness;

    @Override
    public ResponseVo<Class> addNewClass(RequestVo<Class> param) {
        if (param == null || param.getData() == null || StringUtils.isEmpty(param.getToken())
                || StringUtils.isEmpty(param.getData().getClassName())) {
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return classBusiness.addNewClass(param);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVo.getResponseVo(false, null, ConstantEnum.FAIL.getStatusCode(), "新建班级异常:" + e.getMessage());
        }
    }

    @Override
    public ResponseVo<Class> getClassInfoBySelf(String token) {
        if (StringUtils.isEmpty(token)){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return classBusiness.getClassInfoBySelf(token);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getResponseVo(false,null,ConstantEnum.FAIL.getStatusCode(),"获取班级信息失败:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Class> updateClassInfoByTeacher(RequestVo<Class> param) {
        if (param == null || param.getData() == null || StringUtils.isEmpty(param.getToken())) {
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return classBusiness.updateClassInfoByTeacher(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("更新班级信息异常:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> updateUserClassAndClassInfo(RequestVo<String> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || StringUtils.isEmpty(param.getData())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return classBusiness.updateUserClassAndClassInfo(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("加入班级异常:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<User>> getClassMemberBySelf(String token) {
        if (StringUtils.isEmpty(token)){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return classBusiness.getClassMemberBySelf(token);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取班级成员信息异常:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> deleteClassMemberByTeacher(RequestVo<String> param) {
        if (param == null || StringUtils.isEmpty(param.getToken()) || param.getData() == null){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return classBusiness.deleteClassMemberByTeacher(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("移除班级成员异常:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> updateStudentClassInfoByStudent(String token) {
        if (StringUtils.isEmpty(token)){
            return ResponseVo.getParamErrorResponseVo();
        }
        try {
            return classBusiness.updateStudentClassInfoByStudent(token);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("退出班级异常:"+e.getMessage());
        }
    }
}
