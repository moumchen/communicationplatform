package com.jixiata.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.jixiata.model.Vo.ResponseVo;
import com.jixiata.util.ConstantEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 二维码等相关 Controller
 */
@Controller
@RequestMapping("/api")
@Api("数字码相关接口")
public class CodeController {

    @Autowired
    private Producer kaptchaProducer;

    @Value("LOGIN_VERIFICATION_CODE_KEY")
    private String LOGIN_VERIFICATION_CODE_KEY;
    /**
     * 获取验证码，返回验证码token
     */
    @GetMapping("/getVerificationCode")
    @ApiOperation("获取验证码及其图像")
    public void getVerificationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成验证码
        String capText = kaptchaProducer.createText();
        session.setAttribute(LOGIN_VERIFICATION_CODE_KEY, capText);
        //向客户端写出
        BufferedImage bi = kaptchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        try {
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (NumberFormatException e) {

        } finally {
            out.close();
        }
    }

}
