package com.blinkcoder.controller;

import com.blinkcoder.interceptor.AdminInterceptor;
import com.blinkcoder.model.Tag;
import com.jfinal.aop.Before;

/**
 * User: Michael
 * Date: 13-10-10
 * Time: 下午9:27
 */
public class TagController extends MyController {

    @Before(AdminInterceptor.class)
    public void addLabel() {
        Tag tag = getModel(Tag.class);
        boolean result = false;
        String name = tag.get("name");
        Tag nameTag = Tag.dao.getByName(name);
        if (nameTag == null) {
            result = tag.Save();
        }
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void delLabel() {
        boolean result = false;
        int id = getParaToInt("id", 0);
        if (id > 0) {
            Tag tag = Tag.dao.Get(id);
            result = tag.delete();
        }
        renderJson("msg", result);
    }
}
