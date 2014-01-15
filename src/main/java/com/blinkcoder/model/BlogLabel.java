package com.blinkcoder.model;

import com.blinkcoder.kit.ModelKit;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-10
 * Time: 下午9:58
 */
public class BlogLabel extends MyModel<BlogLabel> {
    public static final BlogLabel dao = new BlogLabel();
    private static final String MODEL_CACHE = "bloglabel";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);
    private static final String MODEL_LIST_CACHE = "bloglabel#list";

    @Override
    public BlogLabel Get(int id) {
        return mk.getModel(id);
    }

    @Override
    protected void removeCache() {
        removeModelCache();

    }

    @Override
    protected void removeModelCache() {
        CacheKit.remove(MODEL_CACHE, this.get("id"));
    }

    public List<BlogLabel> getBlogLabelByLabel(int label_id) {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "label_id-" + label_id, "select id from blog_label where label_id = ?", label_id));
    }

    public List<BlogLabel> getBlogLabelByBlog(int blog_id) {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "blog_id-" + blog_id, "select id from blog_label where blog_id = ?", blog_id));
    }

}
