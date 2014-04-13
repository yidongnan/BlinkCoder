package com.blinkcoder.common;


import com.blinkcoder.controller.*;
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
    private Properties conf = null;

//  配置JFinal常量值
    @Override
    public void configConstant(Constants me) {
//    	整个项目配置文件的加载
        conf = loadPropertyFile("classes" + File.separator + "config.txt");
//      获取视图的配置文件
        myConstants.VELOCITY_TEMPLETE_PATH = getProperty("velocity_templete_path");
        /*       
   			在实际应用开发或者是产品部署时，对应着两种模式：
        	开发模式（devMode） 此时 DevMode=true;
   			产品模式（proMode） 此时：DevMode=false;*/
        if (isLocal) {
            me.setDevMode(true);
        }
        
//      设置视图类型
        me.setViewType(ViewType.OTHER);
//      设置mainRenderFactory，接着你可以在控制器中使用自定义的渲染
//      在这里自定义了Velocity
        me.setMainRenderFactory(new VelocityToolboxRenderFactory());

        
//      指定404页面
        me.setError404View(myConstants.VELOCITY_TEMPLETE_PATH + "/404.html");
//      指定500页面
        me.setError500View(myConstants.VELOCITY_TEMPLETE_PATH + "/500.html");

    }


    
//  设置JFinal访问路由
    @Override
    public void configRoute(Routes me) {
        me.add("/action/blog", BlogController.class).add("/action/catalog", CatalogController.class)
                .add("/action/label", LabelController.class).add("/action/link", LinkController.class)
                .add("/action/user", UserController.class).add("/action/qiniu", QiNiuController.class);
    }

    @Override
    public void configPlugin(Plugins me) {

        String jdbcUrl, username, password, driver;	//  url、数据库用户、密码、驱动
        driver = getProperty("driverClass");	//获取驱动
        jdbcUrl = getProperty("jdbcUrl");		//获取url
        username = getProperty("username");		//获取用户名
        password = getProperty("password");		//获取数据库登录密码
        
        //配置Druid数据库连接池
        DruidPlugin druidPlugin = new DruidPlugin(jdbcUrl, username, password, driver);
        //初始化连接池大小，以及最大活跃连接数
        druidPlugin.setInitialSize(3).setMaxActive(10);
        //位项目设置数据库连接池
        me.add(druidPlugin);
        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);

        // 缓存插件
        me.add(new EhCachePlugin(PathKit.getWebRootPath() + File.separator + "WEB-INF" + File
                .separator + "classes" + File.separator + "ehcache.xml"));
        
        //任务调度插件
        me.add(new QuartzPlugin());

        if (isLocal) {
            arp.setShowSql(true);
        }
        
        //建立数据库表明到model的映射关系
        arp.addMapping("blog", Blog.class).addMapping("user", User.class)
                .addMapping("catalog", Catalog.class).addMapping
                ("blog_label", BlogLabel.class).addMapping("label",
                Label.class).addMapping("link", Link.class).addMapping
                ("lucene_task", LuceneTask.class);
        me.add(arp);	//添加ActiveRecordPlus
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {
    }

    @Override
    public void afterJFinalStart() {
        Config.ACCESS_KEY = getProperty("qiniu_access_key");
        Config.SECRET_KEY = getProperty("qiniu_secret_key");
        myConstants.QINIU_BUICKET = getProperty("qiniu_buicket");
        myConstants.QINIU_RETURNURL = getProperty("qiniu_returnUrl");
        myConstants.STATIC_RESOURCE_PATH = getProperty("static_resource_path");
        myConstants.LUCENE_DIR = getProperty("lucene_dir");
    }

    @Override
    public void beforeJFinalStop() {
    }
}
