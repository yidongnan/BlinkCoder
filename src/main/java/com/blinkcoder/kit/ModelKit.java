package com.blinkcoder.kit;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import java.util.List;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-10-11
 * Time: 下午10:22
 */

////model工具类
public class ModelKit {
    private Model dao;		////定义一个model类型的dao成员变量与数据库进行交互
    private String cacheNameForOneModel;	////为model定义一个缓存的名字

    
    ////拥有dao对象变量和缓存的model构造器
    public ModelKit(Model dao, String cacheNameForOneModel) {
        this.dao = dao;
        this.cacheNameForOneModel = cacheNameForOneModel;
    }
    
    ////加载model分页
    public <M> Page<M> loadModelPage(Page<M> page) {
        List<M> modelList = page.getList();	////定义一页中所有的条目
        for (int i = 0; i < modelList.size(); i++) {
            Model model = (Model) modelList.get(i);	////通过get()方法，获取list所有的元素。
            M obj = getModel(model.getInt("id"));	////把从modelList中获取的单个model元素赋值给obj
            modelList.set(i, obj);		////替换之前的modelList中的元素
        }
        return page;
    }
    ////加载model
    public <M> List<M> loadModel(List<M> modelList) {
        for (int i = 0; i < modelList.size(); i++) {
            Model model = (Model) modelList.get(i);
            M obj = getModel(model.getInt("id"));
            modelList.set(i, obj);
        }
        return modelList;
    }

    ////根据表的id获取model。
    public <M> M getModel(int id) {
        final int ID = id;		////定义并未ID赋值
        final Model DAO = dao;	
        return CacheKit.get(cacheNameForOneModel, ID, new IDataLoader() {
            @Override
            public Object load() {
                return DAO.findById(ID);	////通过id查找
            }
        });
    }

}
