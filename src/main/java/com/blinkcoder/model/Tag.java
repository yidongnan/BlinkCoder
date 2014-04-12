package com.blinkcoder.model;

import com.blinkcoder.kit.LinkKit;
import com.blinkcoder.kit.ModelKit;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-10
 * Time: 下午9:58
 */
public class Tag extends MyModel<Tag> {
    public static final Tag dao = new Tag();
    private static final String MODEL_CACHE = "tag";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);
    private static final String MODEL_LIST_CACHE = "tag#list";

    public Tag Get(int id) {
        return mk.getModel(id);
    }

    public String url() {
        return LinkKit.root("tag/" + this.get("name"));
    }

    @Override
    protected void removeCache() {
        CacheKit.remove(MODEL_CACHE, this.get("id"));
    }

    public List<Tag> getAllTag() {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "all", "select id from tag"));
    }

    public Tag getByName(String name) {
        Tag tag = dao.findFirstByCache(MODEL_CACHE, "name" + name,
                "select id from tag where name = ?", name);
        if (tag != null)
            return Get(tag.getInt("id"));
        else
            return null;
    }
}
