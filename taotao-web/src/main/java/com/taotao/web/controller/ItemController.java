package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.bean.ItemQ;
import com.taotao.web.service.ItemService;


@Controller
@RequestMapping("item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView showDetail(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("item");
        // 查询商品的基本数据
        ItemQ item = this.itemService.queryItemById(itemId);
        mv.addObject("item", item);

        // 查询商品描述数据
        ItemDesc itemDesc = this.itemService.queryItemDescByItemId(itemId);
        mv.addObject("itemDesc", itemDesc);

        // 查询商品规格参数数据
        String html = this.itemService.queryItemParamItemByItemId(itemId);
        mv.addObject("itemParam", html);
        return mv;
    }
}
