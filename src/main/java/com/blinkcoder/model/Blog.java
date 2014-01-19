package com.blinkcoder.model;

import com.blinkcoder.kit.ModelKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-10
 * Time: 下午9:58
 */
public class Blog extends MyModel<Blog> {
    public static final Blog dao = new Blog();
    private static final String MODEL_CACHE = "blog";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);
    private static final String MODEL_LIST_CACHE = "blog#list";

    private static final int TOP = 0x01;
    private static final int NORMAL = 0x00;

    public Blog Get(int id) {
        return mk.getModel(id);
    }

    protected void removeCache() {
        CacheKit.remove(MODEL_CACHE, this.get("id"));
        CacheKit.remove(MODEL_CACHE, this.get("global_url"));
        CacheKit.removeAll(MODEL_LIST_CACHE);
    }

    public Blog getByGlobalUrl(String global_url) {
        Blog blog = findFirstByCache(MODEL_CACHE, "golbal_url - " + global_url, "select id from blog where global_url = ?", global_url);
        if (blog == null)
            return null;
        else
            return Get(blog.getInt("id"));
    }

    public boolean isTopBlog() {
        return this.getInt("type") == TOP;
    }

    public Page<Blog> hotBlogList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "hot#blog" + page + pageSize, page, pageSize, "select id", "from blog order by read_count desc"));
    }

    public Page<Blog> topBlogList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "top#blog" + page + pageSize, page, pageSize, "select id", "from blog where type = ? order by id", TOP));
    }

    public Page<Blog> normalBlogList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "normal#blog" + page + pageSize, page, pageSize, "select id", "from blog where type = ? order by id", NORMAL));
    }

    public Page<Blog> topAndNormalBlogList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "all#blog" + page + pageSize, page, pageSize, "select id", "from blog order by type desc, id desc"));
    }

    public Page<Blog> normalByCatalogBlogList(int catalog_id, int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "normal#catalog#" + catalog_id + "#blog" + page + pageSize, page, pageSize, "select id", "from blog where catalog = ?  order by id desc", catalog_id));
    }

    public Page<Blog> normalByLabelBlogList(int label_id, int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "normal#label#" + label_id + "#blog" + page + pageSize, page, pageSize, "SELECT b.*", " from blog b ,blog_label bl where b.id = bl.blog_id and bl.label_id = ? order by b.id", label_id));
    }


    public static void VisitBlog(ConcurrentHashMap<Integer, Integer> datas) {
        Object[][] args = new Object[datas.size()][11];
        int i = 0;
        for (Integer id : datas.keySet()) {
            int count = datas.get(id);
            args[i][0] = id;
            Blog blog = Blog.dao.Get(id);
            args[i][1] = blog.get("title");
            args[i][2] = blog.get("global_url");
            args[i][3] = blog.get("catalog");
            args[i][4] = blog.get("content");
            args[i][5] = blog.get("desc");
            args[i][6] = blog.get("update_time");
            args[i][7] = count;
            args[i][8] = blog.get("comment_count");
            args[i][9] = blog.get("type");
            args[i][10] = count;
        }
        String sql = "INSERT INTO blog VALUES(?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE " +
                "read_count = read_count + ?";
        Db.batch(sql, args, 500);
        for (int id : datas.keySet()) {
            CacheKit.remove(MODEL_CACHE, id);
        }
    }

    public Blog prevBlog(int id) {
        String sql = "select * from blog where id < ?";
        return findFirst(sql, id);
    }

    public Blog nextBlog(int id) {
        String sql = "select * from blog where id > ?";
        return findFirst(sql, id);
    }
}
