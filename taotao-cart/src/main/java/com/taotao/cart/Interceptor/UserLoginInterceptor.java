package com.taotao.cart.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import threadlocal.UserThreadLocal;


import com.taotao.cart.bean.User;
import com.taotao.cart.service.UserService;
import com.taotao.common.util.CookieUtils;


public class UserLoginInterceptor implements HandlerInterceptor {

    public static final String COOKIENAME = "TT_TOKEN";
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        UserThreadLocal.set(null);

        String token = CookieUtils.getCookieValue(request, COOKIENAME);
        if (StringUtils.isEmpty(token)) {
            return true;
        }
        User user = this.userService.queryUserByToken(token);
        if (null == user) {
            //登录超时
            return true;
        }
        UserThreadLocal.set(user);
        //登录成功
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {

    }

}
