package com.jixiata.business;

import com.jixiata.model.Bo.Class;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;

import java.util.List;

public interface IClassBusiness {
    /**
     * 新建班级 权限 教师
     * @param param
     * @return
     */
    ResponseVo<Class> addNewClass(RequestVo<Class> param);

    /**
     * 获取当前用户的班级信息
     * @param token
     * @return
     */
    ResponseVo<Class> getClassInfoBySelf(String token);

    /**
     * 更新班级信息 权限教师
     * @param param
     * @return
     */
    ResponseVo<Class> updateClassInfoByTeacher(RequestVo<Class> param);

    /**
     * 加入班级
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateUserClassAndClassInfo(RequestVo<String> param);

    /**
     * 获取当前用户班级成员信息
     * @param token
     * @return
     */
    ResponseVo<List<User>> getClassMemberBySelf(String token);

    /**
     * 移除班级成员 权限教师
     * @param param
     * @return
     */
    ResponseVo<Boolean> deleteClassMemberByTeacher(RequestVo<String> param);

    /**
     * 退出班级
     * @param token
     * @return
     */
    ResponseVo<Boolean> updateStudentClassInfoByStudent(String token);
}
