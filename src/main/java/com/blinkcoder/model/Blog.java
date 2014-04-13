package com.blinkcoder.model;

import com.blinkcoder.kit.MarkdownKit;
import com.blinkcoder.kit.ModelKit;
import com.blinkcoder.search.Searchable;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-10
 * Time: 下午9:58
 */
public class Blog extends MyModel<Blog> implements Searchable {
    public static final Blog dao = new Blog();	////静态的类成员变量，直接为类所引用而不通过类的对象。
    private static final String MODEL_CACHE = "blog";	////为blog  model设置cache
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);	////为blog model定义一个ModelKit
    private static final String MODEL_LIST_CACHE = "blog#list";

    private static final int TOP = 0x01;
    private static final int NORMAL = 0x00;
    private static final long serialVersionUID = 1441921092958223502L;

    ////获取blog表的id
    public Blog Get(int id) {
        return mk.getModel(id);		////返回
    }

    ////移除缓存
    protected void removeCache() {
        CacheKit.remove(MODEL_CACHE, this.get("id"));
        CacheKit.remove(MODEL_CACHE, this.get("global_url"));
        CacheKit.removeAll(MODEL_LIST_CACHE);
    }

    ////
    public Blog getByGlobalUrl(String global_url) {
        Blog blog = findFirstByCache(MODEL_CACHE, "golbal_url - " + global_url,
                "select id from blog where global_url = ?", global_url);
        if (blog == null)
            return null;
        else
        	////获取blog的id属性
            return Get(blog.getInt("id"));
    }

    /////判断是否为第一篇博客文章
    public boolean isTopBlog() {
        return this.getInt("type") == TOP;
    }

    public Page<Blog> hotBlogList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "hot#blog" + page + pageSize,
                page, pageSize, "select id", "from blog order by read_count desc"));
    }

    public Page<Blog> topBlogList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "top#blog" + page + pageSize,
                page, pageSize, "select id", "from blog where type = ? order by id", TOP));
    }

    public Page<Blog> normalBlogList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE,
                "normal#blog" + page + pageSize, page, pageSize, "select id",
                "from blog where type = ? order by id", NORMAL));
    }

    public Page<Blog> topAndNormalBlogList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "all#blog" + page + pageSize,
                page, pageSize, "select id", "from blog order by type desc, id desc"));
    }

    public Page<Blog> normalByCatalogBlogList(int catalog_id, int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "normal#catalog#" + catalog_id
                + "#blog" + page + pageSize, page, pageSize, "select id",
                "from blog where catalog = ?  order by id desc", catalog_id));
    }

    public Page<Blog> normalByLabelBlogList(int label_id, int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "normal#label#" + label_id +
                "#blog" + page + pageSize, page, pageSize, "SELECT b.*", " from blog b ," +
                "blog_label bl where b.id = bl.blog_id and bl.label_id = ? order by b.id",
                label_id));
    }

    public List<Blog> allBlog() {
        return mk.loadModel(findByCache(MODEL_LIST_CACHE, "all#blog",
                "select id from blog order by id"));
    }

    public static void VisitBlog(ConcurrentHashMap<Integer, Integer> datas) {
        Object[][] args = new Object[datas.size()][2];
        int i = 0;
        for (Integer id : datas.keySet()) {
            Blog blog = Blog.dao.Get(id);
            if (blog == null) break;
            args[i][0] = id;
            args[i][1] = datas.get(id);
            i++;
        }
        String sql = "INSERT INTO blog (id) VALUES(?) ON DUPLICATE KEY UPDATE " +
                "read_count = read_count + ?";
        Db.batch(sql, args, 500);
        for (int id : datas.keySet()) {
            CacheKit.remove(MODEL_CACHE, id);
        }
    }

    public Blog prevBlog(int id) {
        String sql = "select id from blog where id < ? order by id desc";
        Blog blog = findFirstByCache(MODEL_LIST_CACHE, "prev" + id, sql,id);
        if(blog != null)
            return Get(blog.getInt("id"));
        else return null;
    }

    public Blog nextBlog(int id) {
        String sql = "select id from blog where id > ?";
        Blog blog = findFirstByCache(MODEL_LIST_CACHE, "next" + id, sql, id);
        if(blog != null)
            return Get(blog.getInt("id"));
        else return null;
    }

    public String content() {
        return MarkdownKit.parse(this.getStr("content"));
    }

    @Override
    public int getId() {
        return this.getInt("id");
    }

    @Override
    public void setId(int id) {
        this.set("id", id);
    }

    @Override
    public float boost() {
        return 1.0f;
    }

    @Override
    public Map<String, Object> storeDatas() {
        final Blog blog = this;
        return new HashMap<String, Object>() {{
            put("content", blog.get("content"));
        }};
    }

    @Override
    public Map<String, Object> indexDatas() {
        final Blog blog = this;
        return new HashMap<String, Object>() {{
            put("title", blog.get("title"));
            put("content", blog.get("content"));
            List<BlogLabel> blogLabelList = BlogLabel.dao.getBlogLabelByBlog
                    (blog.getInt("id"));
            StringBuilder labelStr = new StringBuilder();
            for (BlogLabel blogLabel : blogLabelList) {
                labelStr.append(Label.dao.Get(blogLabel.getInt("label_id"))
                        .getStr("name"));
            }
            put("labels", labelStr);
        }};
    }

    @Override
    public int compareTo(Searchable o) {
        int cid1 = this.getId();
        int cid2 = o.getId();
        return cid1 - cid2;
    }
}
