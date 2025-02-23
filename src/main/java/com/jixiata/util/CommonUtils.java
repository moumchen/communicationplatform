package com.jixiata.util;

import com.jixiata.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;



public class CommonUtils {

     /*
     * 获得毫秒值加随机码组成keyid
     * @return
     */
    public static String getKeyID(){
        return System.currentTimeMillis()+ UUID.randomUUID().toString().substring(0,4);
    }

    /**
     * 获取当前时间字符串
     * @return
     */
    public static String getCurrentDateString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 生成登录TOKEN
     * @return
     */
    public static String getLoginToken(){
        return "LGN_"+System.currentTimeMillis()+UUID.randomUUID().toString().substring(0,4);
    }

    /**
     * 存储信息到redis
     * @param wriper
     * @param key
     * @param value
     * @param time
     * @return
     */
    public static void storeInfoToRedis(JedisPoolWriper wriper, String key, String value, Integer time){
        JedisPool jedisPool = wriper.getJedisPool();
        Jedis jedis = jedisPool.getResource();
        jedis.set(key,value);
        jedis.expire(key,time);
    }

    /**
     * 通过Redis获取信息
     * @param wriper
     * @param key
     * @return
     */
    public static String getInfoFromRedis(JedisPoolWriper wriper, String key){
        Jedis jedis = null;
        String value = "";
        try {
            jedis = wriper.getJedisPool().getResource();
             value = jedis.get(key);
        } catch (Exception e){
            wriper.getJedisPool().returnBrokenResource(jedis);
            e.printStackTrace();
        } finally {
            wriper.getJedisPool().returnResource(jedis);
        }
        return value;
    }

    /**
     * 设置Redis的键值对有效时间
     * @param wriper
     * @param key
     * @param time
     */
    public static void setRedisKeyExpire(JedisPoolWriper wriper,String key, Integer time){
        Jedis jedis = wriper.getJedisPool().getResource();
        jedis.expire(key,time);
    }

    /**
     * 检查时间字符串是否满足格式
     */
    public static boolean checkDateString(String date){
        if (!StringUtils.isEmpty(date)){
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.parse(date);
                return true;
            } catch (ParseException e){
                return false;
            }
        }
        return false;
    }
}
