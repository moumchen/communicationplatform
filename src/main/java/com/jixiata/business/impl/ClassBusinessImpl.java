package com.jixiata.business.impl;

import com.jixiata.business.IClassBusiness;
import com.jixiata.dao.ClassMapper;
import com.jixiata.dao.UserMapper;
import com.jixiata.model.Bo.Class;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.util.CommonUtils;
import com.jixiata.util.ConstantEnum;
import com.jixiata.util.JedisPoolWriper;
import com.jixiata.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("unchecked")
public class ClassBusinessImpl implements IClassBusiness {

    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisPoolWriper wriper;
    @Override
    public ResponseVo<Class> addNewClass(RequestVo<Class> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (user.getIdentity() != 1){
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        Class data = param.getData();
        data.setAddTime(CommonUtils.getCurrentDateString());
        data.setIsDelete(0);
        data.setStudents("");
        data.setInviteCode(UUID.randomUUID().toString().substring(0,4));
        data.setModifyTime(CommonUtils.getCurrentDateString());
        data.setKeyId(CommonUtils.getKeyID());
        data.setOwner(user.getKeyId());
        Integer rows = classMapper.insertClass(data);
        if (rows < 1){
            throw new RuntimeException("新建班级数据异常");
        }
        // 更新用户表
        user.setClassId(data.getKeyId());
        Integer rows2 = userMapper.updateUserInfoByKeyId(user);
        if (rows2 < 1){
            throw new RuntimeException("更新用户信息异常");
        }
        // 更新
        UserUtils.updateCurrentUserInfo(param.getToken(), user, userMapper, wriper);
        return ResponseVo.getResponseVo(true,data,ConstantEnum.SUCCESS.getStatusCode(),"新建班级成功");
    }

    @Override
    public ResponseVo<Class> getClassInfoBySelf(String token) {
        User user = UserUtils.getCurrentUserByToken(wriper, token);
        if (user == null ){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (StringUtils.isEmpty(user.getClassId())) {
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        List<Class> lists = classMapper.getClassInfoByCondition(new Class().setKeyId(user.getClassId()));
        if (lists == null){
            lists = new ArrayList<>();
        }
        return ResponseVo.getResponseVo(true, lists.get(0), ConstantEnum.SUCCESS.getStatusCode(),"查询成功");
    }

    @Override
    public ResponseVo<Class> updateClassInfoByTeacher(RequestVo<Class> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null ||StringUtils.isEmpty(user.getClassId())){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (user.getIdentity() != 1) {
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        Class data = param.getData();
        data.setModifyTime(CommonUtils.getCurrentDateString());
        if (!StringUtils.isEmpty(data.getInviteCode())){
            // 如果传入的邀请码不为空 更新邀请码
            data.setInviteCode(UUID.randomUUID().toString().substring(0,4));
        }
        data.setKeyId(user.getClassId());
        Integer rows = classMapper.updateClassInfoByKeyId(data);
        return ResponseVo.getResponseVo(true, data, ConstantEnum.SUCCESS.getStatusCode(), "更新成功");
    }

    @Override
    public ResponseVo<Boolean> updateUserClassAndClassInfo(RequestVo<String> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null ){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (!StringUtils.isEmpty(user.getClassId()) || user.getIdentity() != 0) {
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        List<Class> list = classMapper.getClassInfoByCondition(new Class().setInviteCode(param.getData()));
        if (CollectionUtils.isEmpty(list)){
            return ResponseVo.getResponseVo(false, null, ConstantEnum.FAIL.getStatusCode(),"该邀请码无效");
        }
        Class classInfo = list.get(0);
        user.setClassId(classInfo.getKeyId());
        classInfo.setStudents(classInfo.getStudents()+user.getKeyId()+",");
        Integer count = classInfo.getCount();
        classInfo.setCount(count + 1);
        Integer rows = userMapper.updateUserInfoByKeyId(user);
        if (rows < 1){
            throw new RuntimeException("更新用户信息失败");
        }
        Integer rows2 = classMapper.updateClassInfoByKeyId(classInfo);
        if (rows2 < 1){
            throw new RuntimeException("更新班级信息失败");
        }
        // TODO 更新当前用户登录信息
        UserUtils.updateCurrentUserInfo(param.getToken(), user, userMapper, wriper);
        return ResponseVo.getResponseVo(true,true,ConstantEnum.SUCCESS.getStatusCode(),"加入班级成功");
    }

    @Override
    public ResponseVo<List<User>> getClassMemberBySelf(String token) {
        User user = UserUtils.getCurrentUserByToken(wriper, token);
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (StringUtils.isEmpty(user.getClassId())){
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        User condition = new User();
        condition.setClassId(user.getClassId());
        List<User> list = userMapper.getUserInfoByCondition(condition, null, null);
        if (CollectionUtils.isEmpty(list)){
            list = new ArrayList<>();
        }
        return ResponseVo.getResponseVo(true, list, ConstantEnum.SUCCESS.getStatusCode(), "查询成功");
    }

    @Override
    public ResponseVo<Boolean> deleteClassMemberByTeacher(RequestVo<String> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (user.getIdentity() != 1 || StringUtils.isEmpty(user.getClassId())){
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        // 移除班级表信息
        List<Class> classes = classMapper.getClassInfoByCondition(new Class().setKeyId(user.getClassId()));
        if (StringUtils.isEmpty(classes)){
            throw new RuntimeException("数据异常（班级表无维护数据）,请联系管理员解决!");
        }
        Class classInfo = classes.get(0);
        String studentKeyId = param.getData();
        if (classInfo.getStudents().contains(studentKeyId)){
            String oriStudents = classInfo.getStudents();
            String students = oriStudents.replace(studentKeyId + ",", "");
            classInfo.setStudents(students);
        } else{
            throw new RuntimeException("该班级无此学生数据，请检查重试或联系管理员解决！");
        }
        // 更新班级信息
        classInfo.setModifyTime(CommonUtils.getCurrentDateString());
        Integer count = classInfo.getCount();
        classInfo.setCount(count - 1);
        Integer rows = classMapper.updateClassInfoByKeyId(classInfo);
        if (rows < 1){
            throw new RuntimeException("更新班级信息异常，影响行数为0");
        }
        User student = new User();
        student.setClassId("");
        student.setKeyId(studentKeyId);
        student.setModifyTime(CommonUtils.getCurrentDateString());
        Integer rows2 = userMapper.updateUserInfoByKeyId(student);
        if (rows2 < 1){
            throw new RuntimeException("更新用户信息异常，影响行数为0");
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<Boolean> updateStudentClassInfoByStudent(String token) {
        User user = UserUtils.getCurrentUserByToken(wriper, token);
        if (user == null){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        if (user.getIdentity() != 0 || StringUtils.isEmpty(user.getClassId())){
            return ResponseVo.getIdentityDeniedResponseVo();
        }
        Class classCondition = new Class();
        classCondition.setKeyId(user.getClassId());
        List<Class> list = classMapper.getClassInfoByCondition(classCondition);
        if (CollectionUtils.isEmpty(list)){
            throw new RuntimeException("数据异常（无班级信息数据），请联系管理员");
        }
        Class classInfo = list.get(0);
        String oriStudents = classInfo.getStudents();
        String students = oriStudents.replace(user.getKeyId()+",","");
        classInfo.setStudents(students);
        classInfo.setCount(classInfo.getCount()-1);
        Integer rows = classMapper.updateClassInfoByKeyId(classInfo);
        if (rows <1){
            throw new RuntimeException("更新班级信息异常");
        }
        user.setClassId("");
        Integer rows2 = userMapper.updateUserInfoByKeyId(user);
        if (rows2 < 1){
            throw new RuntimeException("更新用户信息异常");
        }
        return ResponseVo.getSuccessResponseVo();
    }


}
