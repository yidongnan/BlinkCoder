package com.blinkcoder.model;

import java.util.List;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-3-3
 * Time: 上午11:19
 */
public class LuceneTask extends MyModel<LuceneTask> {

    public final static transient int TYPE_BLOG = 1;

    public final static transient int OPT_ADD = 1;
    public final static transient int OPT_UPDATE = 2;
    public final static transient int OPT_DELETE = 3;
    public static final LuceneTask dao = new LuceneTask();
    private static final long serialVersionUID = -7229914072198719241L;

    public LuceneTask() {
    }

    public LuceneTask(long obj_id, int obj_type, int opt) {
        this.set("obj_id", obj_id);
        this.set("obj_type", obj_type);
        this.set("opt", opt);
        this.set("status", 0);
    }

    public static void add(int obj_id, int obj_type) {
        new LuceneTask(obj_id, obj_type, OPT_ADD).Save();
    }

    public static void update(int obj_id, int obj_type) {
        new LuceneTask(obj_id, obj_type, OPT_UPDATE).Save();
    }

    public static void delete(int obj_id, int obj_type) {
        new LuceneTask(obj_id, obj_type, OPT_DELETE).Save();
    }

    public List<LuceneTask> list() {
        return dao.find("select * from lucene_task where status = 0");
    }

    @Override
    protected void removeCache() {

    }

    @Override
    public LuceneTask Get(int id) {
        return dao.findById(id);
    }

    public Blog object() {
        if (this.get("obj_type") == TYPE_BLOG)
            return Blog.dao.Get(this.getInt("id"));
        else return null;
    }
}
