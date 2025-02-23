package com.jixiata.util;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class EncryptUtil {

    // 盐获取
    public static String getSalt(){
        return UUID.randomUUID().toString().substring(0,4);
    }

    // MD5 + Salt 加密
    public static String encode(String content, String salt){
        return DigestUtils.md5DigestAsHex((content+salt).getBytes());
    }

    // MD5 + Salt 对比
    public static boolean check(String oriContent,String md5Content, String salt){
        if (StringUtils.isEmpty(oriContent) || StringUtils.isEmpty(md5Content) || StringUtils.isEmpty(salt)){
            return false;
        }
        if (md5Content.equals(DigestUtils.md5DigestAsHex((oriContent+salt).getBytes()))){
            return true;
        } else{
            return false;
        }
    }


}