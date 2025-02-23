package com.jixiata.service;

import com.jixiata.model.Bo.Application;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;

import java.util.List;

public interface IApplicationService {
    /**
     * 新建申请
     * @param param
     * @return
     */
    ResponseVo<Boolean> insertApplcationByStudent(RequestVo<Application> param);

    /**
     * 查询申请
     * @param param
     * @return
     */
    ResponseVo<List<Application>> getApplication(String param);

    /**
     * 更新申请
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateApplication(RequestVo<Application> param);
}
