package com.blinkcoder.plugin.quartz;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

public class QuartzPlugin implements IPlugin {
    private static final String JOB = "job";

    private final Logger logger = Logger.getLogger(getClass());
    private Map<String, Job> jobs = Maps.newHashMap();

    private SchedulerFactory sf;
    private Scheduler sched;
    private String config = "job.properties";
    private Properties properties;

    public QuartzPlugin(String config) {
        this.config = config;
    }

    public QuartzPlugin() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean start() {
        sf = new StdSchedulerFactory();
        try {
            sched = sf.getScheduler();
        } catch (SchedulerException e) {
            Throwables.propagate(e);
        }
        loadProperties();
        Enumeration<Object> enums = properties.keys();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement() + "";
            if (!key.endsWith(JOB) || !isEnableJob(enable(key))) {
                continue;
            }
            String jobClassName = properties.get(key) + "";
            String jobCronExp = properties.getProperty(cronKey(key)) + "";
            Class<? extends Job> clazz = null;
            try {
                clazz = (Class<? extends Job>) Class.forName(jobClassName);
            } catch (ClassNotFoundException e) {
                logger.error("class not found\t" + jobClassName);
                e.printStackTrace();
            }
            JobDetail job = JobBuilder.newJob(clazz).withIdentity(jobClassName,
                    jobClassName).build();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName,
                    jobClassName).withSchedule(CronScheduleBuilder.cronSchedule(jobCronExp))
                    .build();
            Date ft = null;
            try {
                ft = sched.scheduleJob(job, trigger);
                sched.start();
            } catch (SchedulerException e) {
                Throwables.propagate(e);
            }
            logger.debug(job.getKey() + " has been scheduled to run at: " + ft + " and repeat " +
                    "based on expression: "
                    + trigger.getCronExpression());
        }
        return true;
    }

    private String enable(String key) {
        return key.substring(0, key.lastIndexOf(JOB)) + "enable";
    }

    private String cronKey(String key) {
        return key.substring(0, key.lastIndexOf(JOB)) + "cron";
    }

    private boolean isEnableJob(String enableKey) {
        Object enable = properties.get(enableKey);
        return !(enable != null && "false".equalsIgnoreCase((enable + "").trim()));
    }

    private void loadProperties() {
        properties = new Properties();
        InputStream is = QuartzPlugin.class.getClassLoader().getResourceAsStream(config);
        try {
            properties.load(is);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        logger.debug("------------load Propteries---------------");
        logger.debug(properties.toString());
        logger.debug("------------------------------------------");
    }

    @Override
    public boolean stop() {
        try {
            sched.shutdown();
        } catch (SchedulerException e) {
            Throwables.propagate(e);
        }
        return true;
    }

}
