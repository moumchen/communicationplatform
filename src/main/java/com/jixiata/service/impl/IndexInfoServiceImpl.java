package com.jixiata.service.impl;

import com.jixiata.business.IIndexInfoBusiness;
import com.jixiata.model.Bo.IndexInfo;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IIndexInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class IndexInfoServiceImpl implements IIndexInfoService {
    @Autowired
    private IIndexInfoBusiness indexInfoBusiness;

    @Override
    public ResponseVo<List<IndexInfo>> getIndexInfo(String token) {
        try{
            return indexInfoBusiness.getIndexInfo(token);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取首页信息错误:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<String> addIndexInfos(RequestVo<List<IndexInfo>> param) {
        if (param == null || param.getData() == null || StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try{
            return indexInfoBusiness.addIndexInfos(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("添加首页信息错误:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<Boolean> updateIndexInfo(RequestVo<List<IndexInfo>> param) {
        if (param == null || param.getData() == null || StringUtils.isEmpty(param.getToken())){
            return ResponseVo.getParamErrorResponseVo();
        }
        try{
            return indexInfoBusiness.updateIndexInfo(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("更新首页信息错误:"+e.getMessage());
        }
    }

    @Override
    public ResponseVo<List<IndexInfo>> getIndexInfoByPage(RequestVo<String> param) {
        try{
            return indexInfoBusiness.getIndexInfoByPage(param);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseVo.getFailResponseVoByMessage("获取首页信息（分页）错误:"+e.getMessage());
        }
    }
}
