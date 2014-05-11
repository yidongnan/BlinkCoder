package com.blinkcoder.controller;

import com.blinkcoder.kit.FormatKit;
import com.blinkcoder.model.Blog;
import com.blinkcoder.model.Comment;

import java.sql.Timestamp;
import java.util.Date;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 2014/4/20
 * Time: 9:45
 */
public class CommentController extends MyController {

    public void pubComment() {
        Comment comment = getModel(Comment.class);
        int blogId = comment.getInt("blog_id");
        Blog blog = Blog.dao.Get(blogId);
        if (blog != null) {
            comment.set("create_time", new Timestamp(new Date().getTime()));
            comment.set("user_id", loginUser().getInt("id"));
            comment.Save();
            comment.set("content", FormatKit.text(comment.getStr("content")));
            renderJson("msg", comment);
        } else {
            renderJson("msg", "error");
        }

    }

    public void delComment() {
        boolean result = false;
        int commentId = getParaToInt("id", 0);
        Comment comment = Comment.dao.Get(commentId);
        if (comment != null && comment.get("user_id").equals(loginUser().get("id"))) {
            result = comment.Delete();
        }
        renderJson("msg", result);

    }
}
