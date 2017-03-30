package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;


@Controller
public class IndexController {
    @Autowired
    private IndexService indexService;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView index() {

        ModelAndView mvAndView = new ModelAndView("index");
        String indexAD1 = this.indexService.queryIndexAD1();
        //查询首页大广告位的数据
        mvAndView.addObject("indexAD1", indexAD1);

        return mvAndView;
    }
}
