package com.taotao.sso.service;

import java.util.Date;
import java.util.Map;


import com.taotao.sso.mapper.CartMapper;
import com.taotao.sso.pojo.Cart;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private RedisService redisService;

    private static final String REDIS_IDS = "IDS";


    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer REDIS_TIME = 60 * 30;
    private static final String REDIS_KEY = "TOKEN_";

    public Boolean check(String param, Integer type) {

        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(param);
                break;
            case 2:
                record.setPhone(param);
                break;
            case 3:
                record.setEmail(param);
                break;

            default:
                return null;
        }

        return this.userMapper.selectOne(record) == null;
    }

    public Boolean doRegister(User user) {
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        return this.userMapper.insert(user) == 1;


    }

    public String doLogin(String username, String password) throws JsonProcessingException {

        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (user == null) {
            return null;
        }
        //对比密码是否正确
        if (!StringUtils.equals(DigestUtils.md5Hex(password), user.getPassword())) {
            return null;
        }

        //表示存在，将用户存放到REDIS中

        String token = DigestUtils.md5Hex(username + "" + System.currentTimeMillis());

        this.redisService.set(REDIS_KEY + token, MAPPER.writeValueAsString(user), REDIS_TIME);

        //将redis中购物车中的数据设置成该用户

        //从连接池中获取jedis
        ShardedJedis jedis = shardedJedisPool.getResource();
        Map<String, String> map = jedis.hgetAll(REDIS_IDS);
        if (null != map) {
            for (String key : map.keySet()) {
                String jsondata = map.get(key);
                try {
                    Cart cart = MAPPER.readValue(jsondata, Cart.class);
                    cart.setUserId(user.getId());
                    this.cartMapper.insertSelective(cart);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            this.redisService.del(REDIS_IDS);
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXX==================IIIIIIIIIIIIIIIIIIIIIIIIIIIII");
        }
        jedis.close();
        return token;
    }

    public User queryUserByToken(String token) {
        String key = REDIS_KEY + token;
        try {
            String jsonData = this.redisService.get(key);
            if (StringUtils.isEmpty(jsonData)) {
                //登录超时
                return null;
            }
            //刷新用户时间
            this.redisService.expire(key, REDIS_TIME);

            return MAPPER.readValue(jsonData, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }

}
