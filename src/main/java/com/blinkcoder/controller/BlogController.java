package com.blinkcoder.controller;

import com.blinkcoder.interceptor.AdminInterceptor;
import com.blinkcoder.job.VisitCountJob;
import com.blinkcoder.model.Blog;
import com.blinkcoder.model.BlogTag;
import com.blinkcoder.model.LuceneTask;
import com.blinkcoder.model.Tag;
import com.jfinal.aop.Before;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-10-10
 * Time: 下午9:27
 */
public class BlogController extends MyController {

    private final static int max_age = 7200;

    @Before(AdminInterceptor.class)
    public void addBlog() {
        int blogId = -1;
        int tagId = -1;
        Blog blog = getModel(Blog.class);
        boolean result = false;
        String url = blog.get("global_url");
        Blog urlBlog = Blog.dao.getByGlobalUrl(url);
        if (urlBlog == null) {
            blog.set("update_time", new Timestamp(new Date().getTime()));
            blog.set("content", blog.getStr("content"));
            blog.set("owner_id", loginUser().get("id"));
            result = blog.Save();
            blogId = blog.get("id");
            LuceneTask.add(blogId, LuceneTask.TYPE_BLOG);
            BlogTag.dao.delBlogTagByBlog(blogId);
            String[] tagArray = getParaValues("tags");
            if (ArrayUtils.isNotEmpty(tagArray)) {
                for (String tagStr : tagArray) {
                    String tagLowStr = StringUtils.lowerCase(tagStr);
                    Tag tag = Tag.dao.getByName(tagLowStr);
                    if (tag != null) {
                        tagId = tag.get("id");
                    } else {
                        Tag newTag = new Tag();
                        newTag.set("name", tagLowStr);
                        newTag.set("desc", tagStr);
                        newTag.Save();
                        tagId = newTag.get("id");
                    }
                    BlogTag blogTag = new BlogTag();
                    blogTag.set("blog_id", blogId);
                    blogTag.set("tag_id", tagId);
                    blogTag.Save();
                }
            }
        }
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void delBlog() {
        int id = getParaToInt("id", 0);
        Blog blog = Blog.dao.Get(id);
        boolean result = false;
        if (id > 0) {
            result = blog.Delete();
            int blogId = blog.getInt("id");
            LuceneTask.delete(blogId, LuceneTask.TYPE_BLOG);
            BlogTag.dao.delBlogTagByBlog(blogId);
        }
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void updateBlog() {
        int blogId = -1;
        int tagId = -1;
        Blog blog = getModel(Blog.class);
        if (blog.get("type") == null)
            blog.set("type", 0);
        boolean result = false;
        int id = blog.get("id", 0);
        if (id > 0) {
            Blog old = Blog.dao.Get(id);
            if (old != null) {
                blog.set("update_time", new Timestamp(new Date().getTime()));
                blog.set("read_count", old.get("read_count"));
                blog.set("comment_count", old.get("comment_count"));
                result = blog.Update();
                blogId = blog.get("id");
                LuceneTask.update(blogId, LuceneTask.TYPE_BLOG);
                BlogTag.dao.delBlogTagByBlog(blogId);
                String[] tags = getParaValues("tags");
                if (ArrayUtils.isNotEmpty(tags)) {
                    for (String tagStr : tags) {
                        String tagLowStr = StringUtils.lowerCase(tagStr);
                        Tag tag = Tag.dao.getByName(tagLowStr);
                        if (tag != null) {
                            tagId = tag.get("id");
                        } else {
                            Tag newTag = new Tag();
                            newTag.set("name", tagLowStr);
                            newTag.set("desc", tagStr);
                            newTag.Save();
                            tagId = newTag.get("id");
                        }
                        BlogTag blogTag = new BlogTag();
                        blogTag.set("blog_id", blogId);
                        blogTag.set("tag_id", tagId);
                        blogTag.Save();
                    }
                }
            }
        }
        renderJson("msg", result);
    }

    public void viewBlog() {
        int id = _prepare(false);
        if (id > 0)
            VisitCountJob.record(VisitCountJob.TYPE_BLOG, id);
        renderNull();
    }

    private int _prepare(boolean cache) {
        if (cache)
            header("Cache-Control", "private,max-age=" + max_age);
        else
            header("Cache-Control", "no-cache");
        header("Content-Type", "text/javascript");
        return getParaToInt("id", 0);
    }

}
