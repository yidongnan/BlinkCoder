package com.blinkcoder.model;

import com.blinkcoder.kit.ModelKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-10
 * Time: 下午9:58
 */
public class BlogTag extends MyModel<BlogTag> {

    public static final BlogTag dao = new BlogTag();
    private static final long serialVersionUID = 2959072518876421589L;
    private static final String MODEL_CACHE = "bloglabel";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);
    private static final String MODEL_LIST_CACHE = "bloglabel#list";

    @Override
    public BlogTag Get(int id) {
        return mk.getModel(id);
    }

    @Override
    protected void removeCache() {
        CacheKit.remove(MODEL_CACHE, this.get("id"));
        CacheKit.removeAll(MODEL_LIST_CACHE);
    }

    public List<BlogTag> getBlogLabelByTag(int tag_id) {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "tag_id-" + tag_id,
                "select id from blog_tag where tag_id = ?", tag_id));
    }

    public List<BlogTag> getBlogTagByBlog(int blog_id) {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "blog_id-" + blog_id,
                "select id from blog_tag where blog_id = ?", blog_id));
    }

    public int delBlogTagByBlog(int blogId) {
        int result = Db.update("delete from blog_tag where blog_id = ?", blogId);
        removeCache();
        return result;
    }

}
