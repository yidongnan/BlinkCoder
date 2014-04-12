package com.blinkcoder.model;

import com.blinkcoder.kit.LinkKit;
import com.blinkcoder.kit.ModelKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-10
 * Time: 下午9:58
 */
public class Catalog extends MyModel<Catalog> {

    public static final Catalog dao = new Catalog();
    private static final long serialVersionUID = 988988497526793211L;
    private static final String MODEL_CACHE = "catalog";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);
    private static final String MODEL_LIST_CACHE = "catalog#list";
    private static final int SHOW_CATALOG = 1;
    private static final int HIDE_CATALOG = 0;

    public Catalog Get(int id) {
        return mk.getModel(id);
    }

    protected void removeCache() {
        CacheKit.remove(MODEL_CACHE, this.get("id"));
        CacheKit.removeAll(MODEL_LIST_CACHE);
    }

    public boolean isShow() {
        return this.getInt("flag") == SHOW_CATALOG;
    }

    public List<Catalog> getShowCatalog() {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, SHOW_CATALOG,
                "select id from catalog where flag = ?", SHOW_CATALOG));
    }

    public List<Catalog> getHideCatalog() {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, HIDE_CATALOG,
                "select id from catalog where flag = ?", HIDE_CATALOG));
    }

    public List<Catalog> getAllCatalog() {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "all", "select id from catalog"));
    }

    public Catalog getByName(String name) {
        Catalog catalog = dao.findFirstByCache(MODEL_CACHE, "name" + name,
                "select id from catalog where name = ?", name);
        if (catalog != null)
            return Get(catalog.getInt("id"));
        else
            return null;
    }

    public Page<Catalog> catalogList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "catalog" + page + pageSize,
                page, pageSize, "select id", "from catalog order by id"));
    }

    public String url() {
        return LinkKit.root("catalog/" + this.get("name"));
    }

}
