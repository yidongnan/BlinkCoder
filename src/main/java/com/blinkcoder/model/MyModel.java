package com.blinkcoder.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-11-9
 * Time: 上午10:58
 */
public abstract class MyModel<M extends Model> extends Model<M> {



    static final Object[] NULL_PARA_ARRAY = new Object[0];

    
    ////通过缓存查找
    public M findFirstByCache(String cacheName, Object key, String sql, Object... paras) {
        M result = CacheKit.get(cacheName, key);		////获取缓存
        if (result == null) {
            result = findFirst(sql, paras);		//返回查找结果
            CacheKit.put(cacheName, key, result);	////把查找结果放置在cache中
        }
        return result;	////返回查找结果
    }
    /////通过缓存查找
    public M findFirstByCache(String cacheName, Object key, String sql) {
        M result = CacheKit.get(cacheName, key);
        if (result == null) {
            result = findFirst(sql, NULL_PARA_ARRAY);
            CacheKit.put(cacheName, key, result);
        }
        return result;
    }
    /////保存model
    public boolean Save() {
        boolean result = super.save();
        if (result)
            removeCache();
        return result;
    }

    ////更新model
    public boolean Update() {
        boolean result = this.update();
        if (result)
            removeCache();
        return result;
    }

    ////删除model
    public boolean Delete() {
        boolean result = this.delete();
        if (result)
            removeCache();
        return result;
    }

    ////删除缓存
    abstract protected void removeCache();

    abstract public M Get(int id);
}
