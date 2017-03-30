package com.taotao.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.cart.service.CartRedisService;
import com.taotao.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import threadlocal.UserThreadLocal;

import com.taotao.cart.bean.User;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartCookieService;
import com.taotao.cart.service.CartService;

@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartCookieService cartCookieService;
    @Autowired
    private CartRedisService cartRedisService;

    /**
     * 添加商品到购物车
     *
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") Long itemId, HttpServletRequest request,
                                HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (null == user) {
            //表示未登录
            this.cartCookieService.addItemToCart(itemId, request, response);
            this.cartRedisService.addItemToCart(itemId);
        } else {
            //表示已经登录
            this.cartService.addItemToCart(itemId);
        }
        return "redirect:/cart/list.html";
    }

    /**
     * 显示商品
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView showList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("cart");
        User user = UserThreadLocal.get();
        List<Cart> cartList = null;
        if (null == user) {
            //表示未登录
            //cartList = this.cartCookieService.queryCartList(request);
            cartList = this.cartRedisService.queryAllCart();

        } else {
            //表示已经登录
            cartList = this.cartService.queryCartList();

        }
        mv.addObject("cartList", cartList);
        return mv;
    }


    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    @ResponseBody
    public void updateNum(@PathVariable("itemId") Long itemId,
                          @PathVariable("num") Integer num, HttpServletRequest request,
                          HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (null == user) {
            //表示未登录
            this.cartCookieService.updateNum(itemId, num, request, response);
        } else {
            //表示已经登录
            this.cartService.updateNum(itemId, num);
        }
    }

    /**
     * 删除购物车中的商品
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String delete(@PathVariable("itemId") Long itemId, HttpServletRequest request,
                         HttpServletResponse response) {

        User user = UserThreadLocal.get();
        if (null == user) {
            //表示未登录
            this.cartCookieService.delete(itemId, request, response);
            this.cartRedisService.deleteCart(itemId);
        } else {
            //表示已经登录
            this.cartService.delete(itemId);
        }
        return "redirect:/cart/list.html";
    }


}
