package com.blinkcoder.controller;

import com.blinkcoder.interceptor.AdminInterceptor;
import com.blinkcoder.model.Label;
import com.jfinal.aop.Before;

/**
 * User: Michael
 * Date: 13-10-10
 * Time: 下午9:27
 */
public class LabelController extends MyController {

    @Before(AdminInterceptor.class)
    public void addLabel() {
        Label label = getModel(Label.class);
        boolean result = false;
        String name = label.get("name");
        Label nameLabel = Label.dao.getByName(name);
        if (nameLabel == null) {
            result = label.Save();
        }
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void delLabel() {
        boolean result = false;
        int id = getParaToInt("id", 0);
        if (id > 0) {
            Label label = Label.dao.Get(id);
            result = label.delete();
        }
        renderJson("msg", result);
    }
}
