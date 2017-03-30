package com.taotao.web.mq.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;

public class ItemMqHandler {
    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void execute(String msg) {

        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            String key = ItemService.REDIS_KEY + jsonNode.get("itemId").asText();
            this.redisService.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
