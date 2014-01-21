package com.blinkcoder.controller;

import com.blinkcoder.interceptor.AdminInterceptor;
import com.blinkcoder.model.Link;
import com.jfinal.aop.Before;

/**
 * User: Michael
 * Date: 13-10-10
 * Time: 下午9:27
 */
public class LinkController extends MyController {

    @Before(AdminInterceptor.class)
    public void addLink() {
        Link link = getModel(Link.class);
        boolean result = link.Save();
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void delLink() {
        boolean result = false;
        int id = getParaToInt("id", 0);
        if (id > 0) {
            Link link = Link.dao.Get(id);
            result = link.Delete();
        }
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void updateLink() {
        Link link = getModel(Link.class);
        boolean result = false;
        int id = link.get("id", 0);
        if (id > 0) {
            result = link.Update();
        }
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void upLink() {
        int id = getParaToInt("id");
        boolean result = false;
        result = Link.dao.upLink(id);
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void downLink() {
        int id = getParaToInt("id");
        boolean result = false;
        result = Link.dao.downLink(id);
        renderJson("msg", result);
    }
}
