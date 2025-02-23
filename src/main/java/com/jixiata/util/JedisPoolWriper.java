package com.jixiata.util;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 这个类主要用于三个参数构建JedisPool，通过getter setter方法获取JedisPool
 */
public class JedisPoolWriper {
    private JedisPool jedisPool;

    public JedisPoolWriper(JedisPoolConfig jedisPoolConfig,String host,int port) {
        jedisPool = new JedisPool(jedisPoolConfig,host,port);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
