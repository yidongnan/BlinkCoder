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

    public M findFirstByCache(String cacheName, Object key, String sql, Object... paras) {
        M result = CacheKit.get(cacheName, key);
        if (result == null) {
            result = findFirst(sql, paras);
            CacheKit.put(cacheName, key, result);
        }
        return result;
    }

    public M findFirstByCache(String cacheName, Object key, String sql) {
        M result = CacheKit.get(cacheName, key);
        if (result == null) {
            result = findFirst(sql, NULL_PARA_ARRAY);
            CacheKit.put(cacheName, key, result);
        }
        return result;
    }

    public boolean Save() {
        boolean result = super.save();
        if (result)
            removeCache();
        return result;
    }

    public boolean Update() {
        boolean result = this.update();
        if (result)
            removeCache();
        return result;
    }

    public boolean Delete() {
        boolean result = this.delete();
        if (result)
            removeCache();
        return result;
    }

    abstract protected void removeCache();

    abstract public M Get(int id);
}
