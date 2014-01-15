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
public class Label extends MyModel<Label> {
    public static final Label dao = new Label();
    private static final String MODEL_CACHE = "label";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);
    private static final String MODEL_LIST_CACHE = "label#list";

    public Label Get(int id) {
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

    public List<Label> getAllLabel() {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "all", "select id from label"));
    }

    public Label getByName(String name) {
        Label label = dao.findFirstByCache(MODEL_CACHE, "name" + name, "select id from label where name = ?", name);
        if (label != null)
            return Get(label.getInt("id"));
        else
            return null;
    }
}
