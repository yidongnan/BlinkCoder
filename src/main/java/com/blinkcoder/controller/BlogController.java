package com.blinkcoder.controller;

import com.blinkcoder.interceptor.AdminInterceptor;
import com.blinkcoder.job.VisitCountJob;
import com.blinkcoder.kit.HtmlKit;
import com.blinkcoder.model.Blog;
import com.blinkcoder.model.BlogLabel;
import com.blinkcoder.model.Label;
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
        int labelId = -1;
        Blog blog = getModel(Blog.class);
        boolean result = false;
        String url = blog.get("global_url");
        Blog urlBlog = Blog.dao.getByGlobalUrl(url);
        if (urlBlog == null) {
            blog.set("update_time", new Timestamp(new Date().getTime()));
            blog.set("content", HtmlKit.cleanBody(blog.getStr("content")));
            result = blog.Save();
            blogId = blog.get("id");
            BlogLabel.dao.delBlogLabelByBlog(blogId);
            String[] labels = getParaValues("labels");
            if (ArrayUtils.isNotEmpty(labels)) {
                for (String labelStr : labels) {
                    String labelLowStr = StringUtils.lowerCase(labelStr);
                    Label label = Label.dao.getByName(labelLowStr);
                    if (label != null) {
                        labelId = label.get("id");
                    } else {
                        Label newLabel = new Label();
                        newLabel.set("name", labelLowStr);
                        newLabel.set("desc", labelStr);
                        newLabel.Save();
                        labelId = newLabel.get("id");
                    }
                    BlogLabel blogLabel = new BlogLabel();
                    blogLabel.set("blog_id", blogId);
                    blogLabel.set("label_id", labelId);
                    blogLabel.Save();
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
            BlogLabel.dao.delBlogLabelByBlog(blog.getInt("id"));
        }
        renderJson("msg", result);
    }

    @Before(AdminInterceptor.class)
    public void updateBlog() {
        int blogId = -1;
        int labelId = -1;
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
                BlogLabel.dao.delBlogLabelByBlog(blogId);
                String[] labels = getParaValues("labels");
                if (ArrayUtils.isNotEmpty(labels)) {
                    for (String labelStr : labels) {
                        String labelLowStr = StringUtils.lowerCase(labelStr);
                        Label label = Label.dao.getByName(labelLowStr);
                        if (label != null) {
                            labelId = label.get("id");
                        } else {
                            Label newLabel = new Label();
                            newLabel.set("name", labelLowStr);
                            newLabel.set("desc", labelStr);
                            newLabel.Save();
                            labelId = newLabel.get("id");
                        }
                        BlogLabel blogLabel = new BlogLabel();
                        blogLabel.set("blog_id", blogId);
                        blogLabel.set("label_id", labelId);
                        blogLabel.Save();
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
        String user_agent = header("user-agent");
        String referer = header("referer");
        /*if(isRobot()){
            try {
                ctx.not_found();
            } catch (IOException e) {}
            return -1;
        }*/
        /*if(StringUtils.isNotBlank(referer) && referer.indexOf("oschina.net")<0)
            return -3;*/
        return getParaToInt("id", 0);
    }

}
