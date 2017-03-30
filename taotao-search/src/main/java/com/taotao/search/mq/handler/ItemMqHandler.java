package com.taotao.search.mq.handler;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.bean.Item;
import com.taotao.search.service.ItemService;

public class ItemMqHandler {


    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private HttpSolrServer httpSolrServer;
    @Autowired
    private ItemService itemService;

    public void execute(String msg) {

        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            String type = jsonNode.get("type").asText();
            Long itemId = jsonNode.get("itemId").asLong();

            if (StringUtils.equals(type, "insert") || StringUtils.equals(type, "update")) {
                //需要将商品数据写入到solr
                Item item = this.itemService.queryItemById(itemId);
                if (null == item) {
                    return;
                }
                this.httpSolrServer.addBean(item);
                this.httpSolrServer.commit();
            } else if (StringUtils.equals(type, "delete")) {
                //需要将solr中的商品删除
                this.httpSolrServer.deleteById(String.valueOf(itemId));
                this.httpSolrServer.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
