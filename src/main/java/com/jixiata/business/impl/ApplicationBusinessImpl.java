package com.jixiata.business.impl;

import com.jixiata.business.IApplicationBusiness;
import com.jixiata.dao.ApplicationMapper;
import com.jixiata.dao.ClassMapper;
import com.jixiata.model.Bo.Application;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Bo.Class;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.util.CommonUtils;
import com.jixiata.util.ConstantEnum;
import com.jixiata.util.JedisPoolWriper;
import com.jixiata.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class ApplicationBusinessImpl implements IApplicationBusiness {
    @Autowired
    private JedisPoolWriper wriper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private ApplicationMapper applicationMapper;
    @Override
    public ResponseVo<Boolean> insertApplcationByStudent(RequestVo<Application> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        Application data = param.getData();
        data.setAddTime(CommonUtils.getCurrentDateString());
        data.setModifyTime(CommonUtils.getCurrentDateString());
        data.setIsDelete(0);
        data.setIsLock(0);
        data.setKeyId(CommonUtils.getKeyID());
        data.setStudentId(user.getKeyId());

        Class condition = new Class();
        condition.setKeyId(user.getClassId());
        List<Class> classes = classMapper.getClassInfoByCondition(condition);
        Class classInfo = classes.get(0);
        String teacherId = classInfo.getOwner();

        data.setTeacherId(teacherId);
        data.setResult(0);
        data.setResultTime(CommonUtils.getCurrentDateString());

        Integer row = applicationMapper.insertApplication(data);
        if (row == 0){
            throw new RuntimeException("数据插入错误");
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<List<Application>> getApplication(String token) {
        User user = UserUtils.getCurrentUserByToken(wriper, token);
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        Application condition = new Application();
        if (user.getIdentity() == 0){
            // 学生
            condition.setStudentId(user.getKeyId());
        }
        if (user.getIdentity() == 1){
            condition.setTeacherId(user.getKeyId());
        }
        List<Application> temp;
        List<Application> result = new ArrayList<>();
        Integer pageIndex = 1;
        do {
            temp = applicationMapper.getApplicationByCondition(condition, pageIndex, 50);
            result.addAll(temp);
            pageIndex++;
        } while (temp.size() == 50);
        return ResponseVo.getResponseVo(true,result, ConstantEnum.SUCCESS.getStatusCode(),"查询成功");
    }

    @Override
    public ResponseVo<Boolean> updateApplication(RequestVo<Application> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }

        Application data = param.getData();
        data.setModifyTime(CommonUtils.getCurrentDateString());
        if (param.getData().getIsDelete() == null){
            data.setResultTime(CommonUtils.getCurrentDateString());
        }
        Integer row = applicationMapper.updateApplicationByKeyId(data);
        if (row == 0){
            throw new RuntimeException("更新申请数据异常！");
        }
        return ResponseVo.getSuccessResponseVo();
    }
}
