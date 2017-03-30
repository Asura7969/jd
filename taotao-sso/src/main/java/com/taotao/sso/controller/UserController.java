package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.abel533.mapper.Mapper;
import com.taotao.common.util.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService UserService;


    private static final String COOKIENAME = "TT_TOKEN";

    /**
     * 去注册页面
     *
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable("param") String param, @PathVariable("type") Integer type) {

        try {
            Boolean bool = this.UserService.check(param, type);
            if (bool == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            //为了兼容前端JS逻辑，将返回值取反
            return ResponseEntity.ok().body(!bool);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }

    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doRegister(@Valid User user, BindingResult bindingResult) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();

            //校验有错误
            if (bindingResult.hasErrors()) {
                List<String> msgsList = new ArrayList<String>();
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                for (ObjectError objectError : allErrors) {
                    msgsList.add(objectError.getDefaultMessage());
                }
                map.put("status", "400");
                map.put("data", StringUtils.join(msgsList, "|"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);

            }

            Boolean bool = this.UserService.doRegister(user);
            if (bool) {//表示注册成功
                map.put("status", "200");
            } else {
                map.put("status", "566");
                map.put("data", "请重新注册");
            }
            return ResponseEntity.ok().body(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }


    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            String token = this.UserService.doLogin(username, password);
            if (StringUtils.isEmpty(token)) {
                map.put("status", 300);

            } else {
                map.put("status", 200);
                //将token写入到cookie中
                CookieUtils.setCookie(request, response, COOKIENAME, token);
            }
        } catch (Exception e) {

            e.printStackTrace();
            map.put("status", 500);
        }
        return map;

    }

    /**
     * 根据token查询信息
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "{token}", method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token) {
        try {
            User user = this.UserService.queryUserByToken(token);
            if (null == user) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }

}