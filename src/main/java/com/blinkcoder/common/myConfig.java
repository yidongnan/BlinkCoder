package com.blinkcoder.common;


import com.blinkcoder.controller.*;
import com.blinkcoder.handler.UserHandler;
import com.blinkcoder.model.*;
import com.blinkcoder.plugin.quartz.QuartzPlugin;
import com.blinkcoder.render.VelocityToolboxRenderFactory;
import com.jfinal.config.*;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StringKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.qiniu.api.config.Config;

import java.io.File;
import java.util.Properties;

/**
 * User: Michael
 * Date: 13-10-10
 * Time: 下午9:23
 */
public class myConfig extends JFinalConfig {

    private String json = java.lang.System.getenv("VCAP_SERVICES");
    private boolean isLocal = StringKit.isBlank(json);

    @Override
    public void configConstant(Constants me) {
        Properties conf = loadPropertyFile("classes" + File.separator + "config.properties");
        myConstants.VELOCITY_TEMPLETE_PATH = getProperty("velocity_templete_path");
        if (isLocal) {
            me.setDevMode(true);
        }
        me.setViewType(ViewType.OTHER);
        me.setMainRenderFactory(new VelocityToolboxRenderFactory());

        me.setError404View(myConstants.VELOCITY_TEMPLETE_PATH + "/404.html");
        me.setError500View(myConstants.VELOCITY_TEMPLETE_PATH + "/500.html");

    }


    @Override
    public void configRoute(Routes me) {
        me.add("/action/blog", BlogController.class).add("/action/catalog", CatalogController.class)
                .add("/action/tag", TagController.class).add("/action/link", LinkController.class)
                .add("/action/user", UserController.class).add("/action/qiniu", QiNiuController.class)
                .add("action/comment", CommentController.class);
    }

    @Override
    public void configPlugin(Plugins me) {
        String jdbcUrl, username, password, driver;
        driver = getProperty("driverClass");
        jdbcUrl = getProperty("jdbcUrl");
        username = getProperty("username");
        password = getProperty("password");
        DruidPlugin druidPlugin = new DruidPlugin(jdbcUrl, username, password, driver);
        druidPlugin.setInitialSize(3).setMaxActive(10);
        me.add(druidPlugin);


        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);

        // 缓存插件
        me.add(new EhCachePlugin(PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + "classes" + File.separator +
                "ehcache.xml"));
        me.add(new QuartzPlugin());

        if (isLocal) {
            arp.setShowSql(true);
        }
        arp.addMapping("blog", Blog.class).addMapping("user", User.class).addMapping("catalog",
                Catalog.class).addMapping("blog_tag", BlogTag.class).addMapping("tag", Tag.class).addMapping("link",
                Link.class).addMapping("lucene_task", LuceneTask.class).addMapping("comment", Comment.class);
        me.add(arp);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {
        me.add(new UserHandler());
    }

    @Override
    public void afterJFinalStart() {
        Config.ACCESS_KEY = getProperty("qiniu_access_key");
        Config.SECRET_KEY = getProperty("qiniu_secret_key");
        myConstants.QINIU_BUICKET = getProperty("qiniu_buicket");
        myConstants.QINIU_RETURNURL = getProperty("qiniu_returnUrl");
        myConstants.STATIC_RESOURCE_PATH = getProperty("static_resource_path");
        myConstants.LUCENE_DIR = getProperty("lucene_dir");
        myConstants.GOOGLE_CLIENT_ID = getProperty("google_client_id");
        myConstants.GOOGLE_CLIENT_SECRET_KEY = getProperty("google_client_secret_key");
        myConstants.COOKIE_ENCRYPT_KEY = getProperty("cookie_encrypt_key").getBytes();
    }

    @Override
    public void beforeJFinalStop() {
    }
}
