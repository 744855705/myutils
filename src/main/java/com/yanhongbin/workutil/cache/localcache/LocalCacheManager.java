package com.yanhongbin.workutil.cache.localcache;

import com.yanhongbin.workutil.cache.ICache;
import com.yanhongbin.workutil.cache.IRefresh;

/**
 * description: 本地缓存,由 CurrentHashMap 实现, 有缓存过期机制
 *
 * @author yhb
 * @version 1.0
 * @date 2021/2/23 10:39
 */
public class LocalCacheManager implements ICache {

    private static final ICache CACHE_MANAGER = new LocalCacheManager();

    private LocalCacheManager(){}

    @Override
    public <T> T get(String key) {
        return CacheUtil.get(key);
    }

    @Override
    public <T> void put(String key, T value, long expire) {
        CacheUtil.put(key, value, expire);
    }

    @Override
    public void clearAllCache() {
        CacheUtil.clear();
    }

    @Override
    public void delete(String key) {
        CacheUtil.delete(key);
    }

    @Override
    public <T> T getAndRefresh(String key, IRefresh<T> refresh) {
        return CacheUtil.getAndRefresh(key, refresh);
    }

    @Override
    public void clearOverTimeNode() {
        CacheUtil.clearOverTimeNode();
    }

    public static ICache getCacheManager() {
        return CACHE_MANAGER;
    }
}
