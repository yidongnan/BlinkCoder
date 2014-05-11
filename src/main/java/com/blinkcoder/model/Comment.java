package com.blinkcoder.model;

import com.blinkcoder.kit.ModelKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 2014/4/20
 * Time: 9:03
 */
public class Comment extends MyModel<Comment> {

    public static final Comment dao = new Comment();
    private static final long serialVersionUID = 215392092919059880L;
    private static final String MODEL_CACHE = "comment";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);
    private static final String MODEL_LIST_CACHE = "comment#list";

    public Comment Get(int id) {
        return mk.getModel(id);
    }

    protected void removeCache() {
        CacheKit.remove(MODEL_CACHE, this.get("id"));
        CacheKit.removeAll(MODEL_LIST_CACHE);
    }

    public Page<Comment> getCommentListByBlog(int page, int pageSize, int blogId) {
        return mk.loadModelPage(dao.paginateByCache(MODEL_LIST_CACHE, "comment#" + blogId, page, pageSize, "select id",
                "from comment where blog_id = ? order by create_time", blogId));
    }

    public List<Comment> getNewCommentList() {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "newcomment", "select id from comment order by create_time desc limit 10"));
    }

}
