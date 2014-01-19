package com.blinkcoder.controller;

import com.blinkcoder.interceptor.AdminInterceptor;
import com.blinkcoder.kit.HtmlKit;
import com.blinkcoder.model.Blog;
import com.blinkcoder.plugin.visitStat.VisitStatPlugin;
import com.jfinal.aop.Before;

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
        Blog blog = getModel(Blog.class);
        boolean result = false;
        String url = blog.get("global_url");
        Blog urlBlog = Blog.dao.getByGlobalUrl(url);
        if (urlBlog == null) {
            blog.set("update_time", new Timestamp(new Date().getTime()));
            blog.set("content", HtmlKit.cleanBody(blog.getStr("content")));
            result = blog.Save();
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
        }
        renderJson("result", result);
    }

    @Before(AdminInterceptor.class)
    public void updateBlog() {
        Blog blog = getModel(Blog.class);
        if(blog.get("type") == null)
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
            }
        }
        renderJson("msg", result);
    }

    public void viewBlog() {
        int id = _prepare(false);
        if (id > 0)
            VisitStatPlugin.record(VisitStatPlugin.TYPE_BLOG, id);
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
