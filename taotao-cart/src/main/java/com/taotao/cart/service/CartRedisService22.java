package com.taotao.cart.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.ItemQ;
import com.taotao.cart.pojo.Cart;
import com.taotao.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.*;

/**
 * Created by 龚文洲 on 2017/3/27.
 */
@Service
public class CartRedisService22 {
    @Autowired
    private RedisService redisService;
    @Autowired
    private ItemService itemService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String REDIS_IDSS = "IDSS";

    private static final Integer REDIS_TIME_INTEGER = 60 * 60 * 24 * 30 * 12;

    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;


    public void addItemToCart(Long itemId) {
        //从连接池中获取jedis
        ShardedJedis jedis = shardedJedisPool.getResource();

        //Jedis jedis = new Jedis("127.0.0.1", 6379);

        //判断商品在购物车中是否存在
        String data = this.redisService.get(REDIS_IDSS);

        System.out.println("===========================" + data + "==========================");

        if (null == data) {
            //表示不存在，查询该商品数据
            ItemQ itemQ = this.itemService.queryItemQById(itemId);
            //封装数据
            Cart cart = new Cart();
            cart.setItemId(itemId);
            cart.setItemImage(itemQ.getImages()[0]);
            cart.setItemTitle(itemQ.getTitle());
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setNum(1);//TODO  默认设置为1
            cart.setItemPrice(itemQ.getPrice());

            //存放到Redis中
            try {
                System.out.println("===========================第一次添加==========================");
                jedis.append(REDIS_IDSS, MAPPER.writeValueAsString(cart));
                jedis.expire(REDIS_IDSS, REDIS_TIME_INTEGER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("===========================第222222次添加==========================");
            //表示存在,数量增加
            try {
                Cart cart = MAPPER.readValue(data, Cart.class);
                cart.setNum(cart.getNum() + 1);//TODO 默认为1
                cart.setUpdated(new Date());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        jedis.close();
    }

    /**
     * 获取Redis里的购物车
     *
     * @return
     */
    public List<Cart> queryAllCart() {
        //从连接池中获取jedis
        ShardedJedis jedis = shardedJedisPool.getResource();

        String jsonData = this.redisService.get(REDIS_IDSS);

        System.out.println("===========================改写上面的方法==========================");
        System.out.println("===========================查看存入的数据格式==========================");

        Map<String, String> map = jedis.hgetAll(REDIS_IDSS);

        if (null == map) {
            return null;
        }

        //定义一个数组，存放遍历出来的Cart数据
        List<Cart> list = new ArrayList<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            String jsonNode = map.get(key);
            try {
                Cart cart = MAPPER.readValue(jsonNode, Cart.class);
                list.add(cart);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        jedis.close();
        return list;
    }

    /**
     * 删除键和值
     *
     * @param itemId
     * @return
     */
    public Long deleteCart(Long itemId) {
        //从连接池中获取jedis
        ShardedJedis jedis = shardedJedisPool.getResource();
        jedis.close();
        return jedis.hdel(REDIS_IDSS, String.valueOf(itemId));
    }

}
