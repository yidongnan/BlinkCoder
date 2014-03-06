package com.blinkcoder.job;

import com.blinkcoder.kit.SearchKit;
import com.blinkcoder.model.Blog;
import com.blinkcoder.model.LuceneTask;
import com.blinkcoder.search.SearchHelper;
import com.jfinal.log.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.blinkcoder.model.LuceneTask.dao;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 14-3-3
 * Time: 下午12:21
 */
public class LuceneJob implements Job {

    private static final Logger log = Logger.getLogger(LuceneJob.class);

    SearchHelper search = SearchKit.getHolder();

    @Override
    public void execute(JobExecutionContext context) {
        List<LuceneTask> list = dao.list();
        try {
            for (LuceneTask task : list) {
                Blog obj = task.object();
                if (obj != null) {
                    switch (task.getInt("opt")) {
                        case LuceneTask.OPT_ADD:
                            search.add(Arrays.asList(obj));
                            break;
                        case LuceneTask.OPT_DELETE:
                            search.delete(Arrays.asList(obj));
                            break;
                        case LuceneTask.OPT_UPDATE:
                            search.update(Arrays.asList(obj));
                    }
                    task.set("status", 1).Update();
                }
            }
        } catch (IOException e) {
            log.error("obj to list is error" + e);
        }
    }
}
