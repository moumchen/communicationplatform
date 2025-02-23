package com.jixiata.business.impl;

import com.jixiata.business.IIndexInfoBusiness;
import com.jixiata.dao.IndexInfoMapper;
import com.jixiata.model.Bo.IndexInfo;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.util.CommonUtils;
import com.jixiata.util.ConstantEnum;
import com.jixiata.util.JedisPoolWriper;
import com.jixiata.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("unchecked")
public class IndexInfoBusinessImpl implements IIndexInfoBusiness {

    @Autowired
    private IndexInfoMapper indexInfoMapper;
    @Autowired
    private JedisPoolWriper wriper;

    @Override
    public ResponseVo<List<IndexInfo>> getIndexInfo(String token) {
        IndexInfo condition = new IndexInfo();
        int pageIndex = 1;
        List<IndexInfo> res = new ArrayList<>();
        List<IndexInfo> result = null;
        do {
          result = indexInfoMapper.getIndexInfoByCondition(condition, pageIndex, 50);
          res.addAll(result);
        } while (result.size() == 50);
        return ResponseVo.getResponseVo(true, res, ConstantEnum.SUCCESS.getStatusCode(), "查询成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVo<String> addIndexInfos(RequestVo<List<IndexInfo>> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user.getIdentity() != 2){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        String result = "";
        for (IndexInfo indexInfo : param.getData()){
            indexInfo.setAddTime(CommonUtils.getCurrentDateString());
            indexInfo.setIsDelete(0);
            indexInfo.setModifyTime(CommonUtils.getCurrentDateString());
            indexInfo.setKeyId(CommonUtils.getKeyID());
            result = indexInfo.getKeyId();
            Integer row = indexInfoMapper.insertIndexInfo(indexInfo);
            if (row < 1){
                throw new RuntimeException("添加错误");
            }
        }
        return ResponseVo.getResponseVo(true,result, ConstantEnum.SUCCESS.getStatusCode(),"操作成功");
    }

    @Override
    public ResponseVo<Boolean> updateIndexInfo(RequestVo<List<IndexInfo>> param) {
        User user = UserUtils.getCurrentUserByToken(wriper, param.getToken());
        if (user.getIdentity() != 2){
            return ResponseVo.getPermissonDeniedResponseVo();
        }
        for (IndexInfo indexInfo : param.getData()){
            indexInfo.setModifyTime(CommonUtils.getCurrentDateString());
            Integer row = indexInfoMapper.updateIndexInfoByKeyId(indexInfo);
            if (row < 1){
                throw new RuntimeException("更新错误");
            }
        }
        return ResponseVo.getSuccessResponseVo();
    }

    @Override
    public ResponseVo<List<IndexInfo>> getIndexInfoByPage(RequestVo<String> param) {
        IndexInfo condition = new IndexInfo();
        List<IndexInfo> result = indexInfoMapper.getIndexInfoByCondition(condition, param.getPageIndex(), param.getPageSize());
        Integer totalCount = indexInfoMapper.getCountByCondition(condition);
        return ResponseVo.getResponseVo(true, result , ConstantEnum.SUCCESS.getStatusCode(), "查询成功",totalCount);
    }
}
