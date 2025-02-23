package com.jixiata.controller;

import com.jixiata.model.Bo.Auth;
import com.jixiata.model.Bo.User;
import com.jixiata.model.Vo.AllUserInfoResult;
import com.jixiata.model.Vo.RequestVo;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.service.IUserService;
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

/**
 * 用户相关Controller
 */
@Controller
@RequestMapping("/api")
@Api("用户相关接口")
public class UserController {

    @Autowired
    private IUserService userService;

    private String FILE_ADDRESS = this.getClass().getResource("/").getPath();


    @PostMapping("/getUserInfo")
    @ResponseBody
    @ApiOperation("获取当前用户信息")
    public ResponseVo<User> getUserInfo(@RequestBody RequestVo<String> param){
        return userService.getUserInfo(param);
    }
    /**
     * 用户信息维护
     */
    @PostMapping("/updateUserInfo")
    @ResponseBody
    @ApiOperation("用户信息维护")
    public ResponseVo<Boolean> updateUserInfo(@RequestBody RequestVo<User> param){
        return userService.updateUserInfo(param);
    }

    /**
     * 更新Redis缓存中当前用户的信息
     * @param token
     * @return
     */
    @PostMapping("/updateCurrentUserInfo")
    @ResponseBody
    @ApiOperation("更新当前用户缓存信息")
    public ResponseVo<Boolean> updateCurrentUserInfo(@RequestBody String token){
        return userService.updateCurrentUserInfo(token);
    }

    @PostMapping("/updateAuth")
    @ResponseBody
    @ApiOperation("用户权限维护（管理员接口)")
    public ResponseVo<Boolean> updateAuth(@RequestBody RequestVo<Auth> param){
        return userService.updateAuth(param);
    }

    @PostMapping("/getAllUserInfo")
    @ResponseBody
    @ApiOperation("用户信息列表获取接口（管理员接口）")
    public ResponseVo<AllUserInfoResult> getAllUserInfo(@RequestBody RequestVo<Boolean> param){
        return userService.getAllUserInfo(param);
    }

    /*
   上传头像
    */
    @ResponseBody
    @PostMapping("/uploadHeadImg")
    @ApiOperation("上传头像")
    public ResponseVo<String> uploadHeadImg(MultipartFile file){
        String path = FILE_ADDRESS.substring(1,FILE_ADDRESS.length()).replaceAll("/","\\\\").replace("WEB-INF\\classes","resources\\images\\headImg");
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
            String filename = "HEADIMG_"+System.currentTimeMillis()+"."+type;
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