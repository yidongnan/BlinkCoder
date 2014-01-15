package com.blinkcoder.plugin.redis;

import com.jfinal.plugin.activerecord.cache.ICache;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-10-30
 * Time: 下午6:28
 */
public class Redis implements ICache {
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key) {
        return (T) RedisKit.get(cacheName, key);
    }

    public void put(String cacheName, Object key, Object value) {
        RedisKit.put(cacheName, key, value);
    }

    public void remove(String cacheName, Object key) {
        RedisKit.remove(cacheName, key);
    }

    public void removeAll(String cacheName) {
        RedisKit.removeAll(cacheName);
    }
}
