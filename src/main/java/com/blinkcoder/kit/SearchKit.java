package com.blinkcoder.kit;

import com.blinkcoder.common.myConstants;
import com.blinkcoder.model.Blog;
import com.blinkcoder.search.SearchHelper;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-3-3
 * Time: 下午1:49
 */
public class SearchKit {

    private final static Logger log = Logger.getLogger(SearchKit.class);

    private final static String _g_lucene_path = PathKit.getWebRootPath() +
            File.separator + "WEB-INF" + File.separator + myConstants
            .LUCENE_DIR + File.separator;

    private final static SearchHelper g_holder;

    static {
        try {
            g_holder = SearchHelper.init(_g_lucene_path);
        } catch (IOException e) {
            throw new RuntimeException("Unabled to init index repository.", e);
        }
    }

    public static SearchHelper getHolder() {
        return g_holder;
    }

    public static Page<Blog> findBlogs(String key, boolean only_subject,
                                       boolean sort_by_time, int p, int count) {
        try {
            Sort sort = sort_by_time ? new Sort(new SortField(
                    SearchHelper.FN_ID, SortField.Type.INT, true)) : new Sort(
                    new SortField("title", SortField.Type.SCORE),
                    new SortField("content", SortField.Type.SCORE),
                    new SortField(SearchHelper.FN_ID, SortField.Type.INT,
                            true));
            List<Integer> ids = g_holder.find(Blog.class, makeBlogQuery(key,
                    only_subject), null, sort, p, count);
            List<Blog> blogList = new ArrayList<>();
            for (int id : ids) {
                blogList.add(Blog.dao.Get(id));
            }
            return new Page<>(blogList, p, count, countBlogs(key,
                    only_subject), blogList.size());
        } catch (IOException e) {
            log.error("find Blogs failed, key=" + key + "," +
                    "only_subject=" + only_subject, e);
        }
        return null;
    }

    public static int countBlogs(String key, boolean only_subject) {
        try {
            return g_holder.count(Blog.class, makeBlogQuery(key,
                    only_subject), null);
        } catch (IOException e) {
            log.error("countBlogs failed, key=" + key + "," +
                    "only_subject=" + only_subject, e);
        }
        return -1;
    }

    private static Query makeBlogQuery(String key, boolean only_subject) {

        key = QueryParser.escape(IKAnalyzerKit.cleanupKey(key));

        BooleanQuery query = new BooleanQuery();

        BooleanQuery complexQuery = new BooleanQuery();
        complexQuery.add(SearchHelper.makeQuery("title", key, 50.0f),
                BooleanClause.Occur.SHOULD);
        if (!only_subject) {
            complexQuery.add(SearchHelper.makeQuery("content", key, 1.0f),
                    BooleanClause.Occur.SHOULD);
            complexQuery.add(SearchHelper.makeQuery("tags", key, 20.0f),
                    BooleanClause.Occur.SHOULD);
        }
        query.add(complexQuery, BooleanClause.Occur.MUST);
        return query;
    }

}
