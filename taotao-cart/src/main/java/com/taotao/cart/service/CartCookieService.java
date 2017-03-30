package com.taotao.cart.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.ItemQ;
import com.taotao.cart.pojo.Cart;
import com.taotao.common.util.CookieUtils;

@Service
public class CartCookieService {


    public static final String COOKIE_NAME = "TT_CART";
    @Autowired
    private ItemService itemService;


    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer COOKIE_TIME_INTEGER = 60 * 60 * 24 * 30 * 12;


    public List<Cart> queryCartList(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        try {

            return MAPPER.readValue(cookieValue,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Cart>(0);
    }

    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {

        List<Cart> cartList = this.queryCartList(request);
        //判断该购物车中是否有商品存在,如果存在，数量相加，反之直接添加
        Cart cart = null;
        for (Cart c : cartList) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
                break;
            }

        }
        if (null == cart) {
            //不存在
            cart = new Cart();

            //TODO
            cart.setItemId(itemId);
            cart.setNum(1);
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());

            //查询商品的基本信息
            ItemQ itemQ = this.itemService.queryItemQById(itemId);
            if (null == itemQ) {
                return;
            }
            String[] imgs = itemQ.getImages();
            if (null != imgs && imgs.length > 0) {
                cart.setItemImage(itemQ.getImages()[0]);
            }
            cart.setItemTitle(itemQ.getTitle());
            cart.setItemPrice(itemQ.getPrice());

            cartList.add(cart);

        } else {
            //存在
            cart.setNum(cart.getNum() + 1);//TODO
            cart.setUpdated(new Date());
        }

        writeCookie(request, response, cartList);


    }

    /**
     * 修改商品数量
     *
     * @param itemId
     * @param num
     * @param request
     * @param response
     */
    public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {

        List<Cart> cartList = this.queryCartList(request);
        Cart cart = null;
        for (Cart c : cartList) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
                break;
            }
        }

        if (null == cart) {
            return;
        }
        cart.setNum(num);
        cart.setUpdated(new Date());
        writeCookie(request, response, cartList);
    }

    public void delete(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> cartList = this.queryCartList(request);
        Cart cart = null;
        for (Cart c : cartList) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cartList.remove(c);
                break;
            }
        }
        writeCookie(request, response, cartList);

    }

    private void writeCookie(HttpServletRequest request, HttpServletResponse response, List<Cart> cartList) {
        try {
            CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(cartList),
                    COOKIE_TIME_INTEGER, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
