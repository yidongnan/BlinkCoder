package com.blinkcoder.job;

import com.blinkcoder.model.Blog;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-2-4
 * Time: 下午4:22
 */
public class VisitCountJob implements Job {

    public final static transient byte TYPE_BLOG = 0x01;
    /**
     * 支持统计的对象类型
     */
    private final static byte[] TYPES = new byte[]{
            TYPE_BLOG
    };


    private final static ConcurrentHashMap<Byte, ConcurrentHashMap<Integer, Integer>> queues =
            new ConcurrentHashMap<Byte, ConcurrentHashMap<Integer, Integer>>() {
                private static final long serialVersionUID = 3094140348751410779L;

                {
                    for (byte type : TYPES)
                        put(type, new ConcurrentHashMap<Integer, Integer>());
                }
            };


    public static void record(byte type, int obj_id) {
        ConcurrentHashMap<Integer, Integer> queue = queues.get(type);
        if (queue != null) {
            Integer nCount = queue.get(obj_id);
            nCount = (nCount == null) ? 1 : nCount + 1;
            queue.put(obj_id, nCount);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        for (byte type : TYPES) {
            ConcurrentHashMap<Integer, Integer> queue = queues.remove(type);
            queues.put(type, new ConcurrentHashMap<Integer, Integer>());
            _flush(type, queue);
        }
    }

    private void _flush(byte type, ConcurrentHashMap<Integer, Integer> queue) {
        if (queue == null || queue.size() == 0)
            return;
        switch (type) {
            case TYPE_BLOG:
                Blog.VisitBlog(queue);
                break;
        }
    }
}
