package com.taotao.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taotao.web.service.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import threadlocal.UserThreadLocal;

import com.taotao.web.Interceptor.UserLoginInterceptor;
import com.taotao.web.bean.Cart;
import com.taotao.web.bean.ItemQ;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;

@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    //@Autowired
    //private CartRedisService cartRedisService;

    @ResponseBody
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("order");
        ItemQ itemQ = this.itemService.queryItemById(itemId);
        mv.addObject("itemQ", itemQ);
        return mv;
    }

    /**
     * 基于购物车实现下单
     *
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView toCartToOrder() {
        ModelAndView mv = new ModelAndView("order-cart");
        List<Cart> carts = this.cartService.queryCartList();

        //List<Cart> carts = this.cartRedisService.queryCartList();


        mv.addObject("carts", carts);
        return mv;
    }


    /**
     * 订单提交
     *
     * @param order
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    public Map<String, Object> submit(Order order) {
        //查询用户信息，设置用户数据
        User user = UserThreadLocal.get();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        Map<String, Object> result = new HashMap<String, Object>();
        String orderId = this.orderService.submit(order);

        System.out.println(orderId);
        if (StringUtils.isEmpty(orderId)) {
            //订单提交失败
            result.put("status", 300);

        } else {
            //提交成功
            result.put("status", 200);
            result.put("data", orderId);
        }
        System.out.println(result.get("status"));
        System.out.println(result);
        return result;

    }

    @RequestMapping(value = "success", method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id") String orderId) {
        ModelAndView mv = new ModelAndView("success");
        Order order = this.orderService.queryOrderById(orderId);
        mv.addObject("order", order);
        // 时间，当前时间向后推2天
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }

}
