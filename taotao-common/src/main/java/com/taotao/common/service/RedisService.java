package com.taotao.common.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RedisService {
    //如果spring容器中有对象就注入，没有就忽略
    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    private <T> T execute(Function<T, ShardedJedis> fun) {
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return fun.callback(shardedJedis);
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
    }

    /**
     * 执行SET操作
     *
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        return this.execute(new Function<String, ShardedJedis>() {

            @Override
            public String callback(ShardedJedis e) {
                return e.set(key, value);
            }
        });
    }

    /**
     * 判断指定键的值是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(final String key) {
        return this.execute(new Function<Boolean, ShardedJedis>() {

            @Override
            public Boolean callback(ShardedJedis e) {
                return e.exists(key);
            }
        });
    }


    /**
     * 获取哈希中的所有数据
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(final String key) {
        return this.execute(new Function<Map<String, String>, ShardedJedis>() {

            @Override
            public Map<String, String> callback(ShardedJedis e) {
                return e.hgetAll(key);
            }
        });
    }

    /**
     * 删除哈希中的指定值
     *
     * @param key
     * @param fields
     * @return
     */
    public Long hdel(final String key, final String fields) {
        return this.execute(new Function<Long, ShardedJedis>() {

            @Override
            public Long callback(ShardedJedis e) {
                return e.hdel(key, fields);
            }
        });
    }

    /**
     * 添加Map
     *
     * @param key
     * @param fields
     * @param value
     * @return
     */
    public String hmset(final String key, final String fields, final String value) {
        return this.execute(new Function<String, ShardedJedis>() {

            @Override
            public String callback(ShardedJedis e) {
                Map<String, String> map = new HashMap<>();
                map.put(fields, value);
                return e.hmset(key, map);
            }
        });
    }

    /**
     * 获取Map
     *
     * @param key
     * @param fields
     * @return
     */
    public List<String> hmget(final String key, final String fields) {
        return this.execute(new Function<List<String>, ShardedJedis>() {

            @Override
            public List<String> callback(ShardedJedis e) {
                return e.hmget(key, fields);
            }
        });
    }


    /**
     * 获取指定键的值
     *
     * @param key
     * @param fields
     * @return
     */
    public String hget(final String key, final String fields) {
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis e) {
                return e.hget(key, fields);
            }
        });
    }


    /**
     * 执行GET操作
     *
     * @param key
     * @param
     * @return
     */
    public String get(final String key) {
        return this.execute(new Function<String, ShardedJedis>() {

            @Override
            public String callback(ShardedJedis e) {
                return e.get(key);
            }
        });
    }

    /**
     * 执行删除操作
     *
     * @param key
     * @return
     */
    public Long del(final String key) {
        return this.execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis e) {
                return e.del(key);
            }
        });
    }

    /**
     * 执行expire操作
     *
     * @param key
     * @return
     */
    public Long expire(final String key, final Integer seconds) {
        return this.execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis e) {
                return e.expire(key, seconds);
            }
        });
    }

    /**
     * 执行set操作并且设置生存时间
     *
     * @param key
     * @param seconds
     * @return
     */
    public String set(final String key, final String value, final Integer seconds) {
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis e) {
                String str = e.set(key, value);
                e.expire(key, seconds);
                return str;
            }
        });
    }


}
