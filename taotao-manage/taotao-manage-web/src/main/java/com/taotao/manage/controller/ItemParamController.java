package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamService;

@Controller
@RequestMapping("item/param")
public class ItemParamController {
    @Autowired
    private ItemParamService ItemParamService;

    @RequestMapping(value = "{itemcatid}", method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryById(@PathVariable("itemcatid") Long itemcatid) {

        try {
            ItemParam itemParam = new ItemParam();
            itemParam.setItemCatId(itemcatid);
            ItemParam itemP = this.ItemParamService.queryOne(itemParam);
            if (null == itemP) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "{itemId}", method = RequestMethod.POST)
    public ResponseEntity<Void> addItemParam(@PathVariable("itemId") Long itemId,
                                             @RequestParam("paramData") String paramData) {
        try {
            ItemParam record = new ItemParam();
            record.setId(null);
            record.setItemCatId(itemId);
            record.setParamData(paramData);
            this.ItemParamService.save(record);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    /**
     * 显示模板数据
     *
     * @param page
     * @param rows
     * @return
     */
    //@RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {

        try {
            EasyUIResult easyUIResult = this.ItemParamService.queryItemParamList(page, rows);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

    }


}
