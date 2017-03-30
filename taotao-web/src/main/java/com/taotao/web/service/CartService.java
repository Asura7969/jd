package com.taotao.web.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import threadlocal.UserThreadLocal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Cart;

@Service
public class CartService {

    @Autowired
    private ApiService apiService;
    @Value("${TAOTAO_CART_URL}")
    private String TAOTAO_CART_URL;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public List<Cart> queryCartList() {

        String url = TAOTAO_CART_URL + "/service/api/cart/" + UserThreadLocal.get().getId();
        try {
            String jsonNode = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonNode)) {
                return null;
            }
            return MAPPER.readValue(jsonNode, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
