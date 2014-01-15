package com.blinkcoder.controller;

import com.blinkcoder.interceptor.AdminInterceptor;
import com.blinkcoder.model.Catalog;
import com.jfinal.aop.Before;

/**
 * User: Michael
 * Date: 13-10-10
 * Time: 下午9:27
 */
public class CatalogController extends MyController {

    @Before(AdminInterceptor.class)
    public void addCatalog() {
        Catalog catalog = getModel(Catalog.class);
        boolean result = false;
        String name = catalog.get("name");
        Catalog nameCatalog = Catalog.dao.getByName(name);
        if (nameCatalog == null) {
            result = catalog.Save();
        }
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void updateCatalog() {
        Catalog catalog = getModel(Catalog.class);
        boolean result = false;
        int id = catalog.get("id", 0);
        if (id > 0) {
            result = catalog.Update();
        }
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void delCatalog() {
        boolean result = false;
        int id = getParaToInt("id", 0);
        if (id > 0) {
            Catalog catalog = Catalog.dao.Get(id);
            result = catalog.delete();
        }
        renderJson("msg", result);
    }
}
