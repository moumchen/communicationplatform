package com.jixiata.controller;


import com.jixiata.model.Bo.IndexInfo;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IIndexInfoService;
import com.jixiata.util.ConstantEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@Api("首页信息相关API")
@RequestMapping("/api")
public class IndexInfoController {

    @Autowired
    private IIndexInfoService indexInfoService;

    private String FILE_ADDRESS = this.getClass().getResource("/").getPath();


    @PostMapping("/getIndexInfo")
    @ResponseBody
    @ApiOperation("获取首页信息")
    public ResponseVo<List<IndexInfo>> getIndexInfo(@RequestBody String token){
        return indexInfoService.getIndexInfo(token);
    }

    @PostMapping("/addIndexInfos")
    @ResponseBody
    @ApiOperation("添加首页信息")
    public ResponseVo<String> addIndexInfos(@RequestBody RequestVo<List<IndexInfo>> param){
        return indexInfoService.addIndexInfos(param);
    }

    @PostMapping("/updateIndexInfos")
    @ResponseBody
    @ApiOperation("更新首页信息")
    public ResponseVo<Boolean> updateIndexInfo(@RequestBody RequestVo<List<IndexInfo>> param){
        return indexInfoService.updateIndexInfo(param);
    }

    @PostMapping("/getIndexInfoByPage")
    @ResponseBody
    @ApiOperation("获取首页公告信息（分页)")
    public ResponseVo<List<IndexInfo>> getIndexInfoByPage(@RequestBody RequestVo<String> param) {
        return indexInfoService.getIndexInfoByPage(param);
    }
    /*
     上传轮播图
      */
    @ResponseBody
    @PostMapping("/uploadInfoImg")
    @ApiOperation("上传轮播图")
    public ResponseVo<String> uploadInfoImg(MultipartFile file){
        String path = FILE_ADDRESS.substring(1,FILE_ADDRESS.length()).replaceAll("/","\\\\").replace("WEB-INF\\classes","resources\\images");
        /// / 文件处理
        if (file == null){
            return ResponseVo.getFailResponseVoByMessage("文件为空");
        }
        File pathFile = new File(path);
        if (!pathFile.exists()){
            pathFile.mkdir();
        }
        String originalFilename = file.getOriginalFilename();
        String type = originalFilename.substring(originalFilename.indexOf('.')+1, originalFilename.length());
        if ("jpeg".equals(type) || "jpg".equals(type) || "png".equals(type)){
            String filename = "LUNBO_"+System.currentTimeMillis()+"."+type;
            try {
                file.transferTo(new File(path+"\\"+filename));
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseVo.getFailResponseVoByMessage("保存文件出错："+e.getMessage());
            }
            return ResponseVo.getResponseVo(true,filename, ConstantEnum.SUCCESS.getStatusCode(),"操作成功!");
        } else {
            return ResponseVo.getFailResponseVoByMessage("文件格式不支持!请上传以下格式文件:jpg/jpeg、png");
        }
    }

}
