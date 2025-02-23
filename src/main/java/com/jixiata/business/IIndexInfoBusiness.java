package com.jixiata.business;

import com.jixiata.model.Bo.IndexInfo;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;

import java.util.List;

public interface IIndexInfoBusiness {
    /**
     * 获取首页信息
     * @param token
     * @return
     */
    ResponseVo<List<IndexInfo>> getIndexInfo(String token);

    /**
     * 添加
     * @param param
     * @return
     */
    ResponseVo<String> addIndexInfos(RequestVo<List<IndexInfo>> param);

    /**
     * 修改
     * @param param
     * @return
     */
    ResponseVo<Boolean> updateIndexInfo(RequestVo<List<IndexInfo>> param);

    /**
     * 分页
     * @param param
     * @return
     */
    ResponseVo<List<IndexInfo>> getIndexInfoByPage(RequestVo<String> param);
}
