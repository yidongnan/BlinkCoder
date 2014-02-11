package com.blinkcoder.model;

import com.blinkcoder.kit.ModelKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 13-10-10
 * Time: 下午9:58
 */
public class Link extends MyModel<Link> {

    private static final long serialVersionUID = -8593116159731536862L;

    public static final Link dao = new Link();
    private static final String MODEL_CACHE = "link";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);
    private static final String MODEL_LIST_CACHE = "link#list";

    public Link Get(int id) {
        return mk.getModel(id);
    }

    protected void removeCache() {
        CacheKit.remove(MODEL_CACHE, this.get("id"));
        CacheKit.removeAll(MODEL_LIST_CACHE);
    }

    @Override
    public boolean Save() {
        int maxOrder = getMaxSequence() + 1;
        this.set("sequence", maxOrder);
        return super.Save();
    }

    public List<Link> getAllLink() {
        return mk.loadModel(dao.findByCache(MODEL_LIST_CACHE, "all",
                "select id from link order by sequence"));
    }

    public Page<Link> linkList(int page, int pageSize) {
        return mk.loadModelPage(paginateByCache(MODEL_LIST_CACHE, "link" + page + pageSize, page,
                pageSize, "select id", "from link order by sequence"));
    }

    public int getMaxSequence() {
        String sql = "select max(sequence) from link";
        List<Record> list = Db.findByCache(MODEL_LIST_CACHE, "max#sequence", sql);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0).getInt("max(sequence)");
        }
        return 1;
    }

    public Link getLinkFromSequence(int sequence) {
        String sql = "select id from link where sequence = ?";
        return Get(findFirstByCache(MODEL_CACHE, "sequence#" + sequence, sql,
                sequence).getInt("id"));
    }

    public boolean upLink(int id) {
        Link link = Get(id);
        int sequence = link.getInt("sequence");
        if (sequence == 1) {
            return true;
        } else {
            Link prevLink = getLinkFromSequence(sequence - 1);
            prevLink.set("sequence", sequence);
            link.set("sequence", sequence - 1);
            prevLink.Update();
            link.Update();
        }
        return true;
    }

    public boolean downLink(int id) {
        Link link = Get(id);
        int sequence = link.getInt("sequence");
        if (sequence == getMaxSequence()) {
            return true;
        } else {
            Link nextLink = getLinkFromSequence(sequence + 1);
            nextLink.set("sequence", sequence);
            link.set("sequence", sequence + 1);
            nextLink.Update();
            link.Update();
        }
        return true;
    }
}
