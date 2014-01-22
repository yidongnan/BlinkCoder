package com.blinkcoder.plugin.visitStat;

import com.blinkcoder.model.Blog;
import com.jfinal.plugin.IPlugin;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-1-5
 * Time: 上午9:38
 */
public class VisitStatPlugin extends TimerTask implements IPlugin {

    public final static transient byte TYPE_BLOG = 0x01;

    public static VisitStatPlugin daemon;
    private static Timer click_timer;
    private final static long INTERVAL = 300 * 1000;

    /**
     * 支持统计的对象类型
     */
    private final static byte[] TYPES = new byte[]{
            TYPE_BLOG
    };


    private final static ConcurrentHashMap<Byte, ConcurrentHashMap<Integer, Integer>> queues =
            new ConcurrentHashMap<Byte, ConcurrentHashMap<Integer, Integer>>() {
                {
                    for (byte type : TYPES)
                        put(type, new ConcurrentHashMap<Integer, Integer>());
                }

                private static final long serialVersionUID = 3094140348751410779L;
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
    public boolean start() {
        daemon = new VisitStatPlugin();
        click_timer = new Timer("VisitStatPlugin", true);
        click_timer.schedule(daemon, INTERVAL, INTERVAL);
        return true;
    }

    @Override
    public boolean stop() {
        click_timer.cancel();
        daemon.cancel();
        return false;
    }

    @Override
    public void run() {
        for (byte type : TYPES) {
            ConcurrentHashMap<Integer, Integer> queue = queues.remove(type);
            queues.put(type, new ConcurrentHashMap<Integer, Integer>());
            _flush(type, queue);
        }
    }

    @Override
    public boolean cancel() {
        boolean b = super.cancel();
        this.run();
        return b;
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
