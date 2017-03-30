package com.taotao.cart.bean;

import org.apache.commons.lang3.StringUtils;

import com.taotao.manage.pojo.Item;

public class ItemQ extends Item {

    public String[] getImages() {

        return StringUtils.split(super.getImage(), ',');
    }


}
