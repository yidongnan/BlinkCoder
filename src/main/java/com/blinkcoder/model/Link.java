package com.blinkcoder.model;

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
public class Link extends MyModel<Link> {
    public static final Link dao = new Link();
    private static final String MODEL_CACHE = "link";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);
    private static final String MODEL_LIST_CACHE = "link#list";

    public Link Get(int id) {
        return mk.getModel(id);
    }

    protected void removeCache() {
        CacheKit.remove(MODEL_CACHE, this.get("id"));
    }

    public List<Link> getAllLink() {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "all", "select id from link order by id"));
    }

    public Page<Link> linkList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "link" + page + pageSize, page, pageSize, "select id", "from link order by id"));
    }
}
