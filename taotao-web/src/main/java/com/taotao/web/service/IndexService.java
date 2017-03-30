package com.taotao.web.service;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;

@Service
public class IndexService {
    @Value("${taotao_manage_url}")
    private String taotao_manage_url;

    @Value("${index_ad1_url}")
    private String index_ad1_url;

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String queryIndexAD1() {
        try {
            String urlString = taotao_manage_url + index_ad1_url;
            String jsonDataString = this.apiService.doGet(urlString);
            if (null == jsonDataString) {
                return null;
            }


            //反序列化为jsonNode对象
            JsonNode jsonNode = MAPPER.readTree(jsonDataString);
            //获取rows数据
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");

            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            //封装自己所需要的对象
            for (JsonNode row : rows) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("srcB", row.get("pic").asText());
                map.put("height", 240);
                map.put("alt", row.get("title").asText());
                map.put("width", 670);
                map.put("src", row.get("pic").asText());
                map.put("widthB", 550);
                map.put("href", row.get("url").asText());
                map.put("heightB", 240);
                result.add(map);

            }
            //将对象序列化为json数据返回
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }
}
