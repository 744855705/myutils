package com.yanhongbin.workutil.cache;

import java.util.concurrent.TimeUnit;

/**
 * Created with IDEA
 * description : cache 抽象，实现此接口以实现缓存功能
 *
 * @author ：yanhongbin
 * @date : Created in 2020/12/11 6:51 下午
 */
public interface ICache {

    /**
     * 从缓存中获取数据
     * @param key key
     * @param <T> 数据类型
     * @return value
     */
    <T> T get(String key);

    /**
     * 放入缓存
     * @param key key
     * @param value value
     * @param <T> value类型
     */
    <T> void put(String key, T value);

    /**
     * 放入缓存，声明过期时间
     * @param key key
     * @param value value
     * @param expire 过期时间,单位秒
     * @param <T> value 类型
     */
    default <T> void put(String key, T value, long expire){
        put(key, value, expire,TimeUnit.SECONDS);
    }

    /**
     * 放入缓存，声明过期时间
     * @param key key
     * @param value value
     * @param expire 过期时间
     * @param unit 过期时间单位
     * @param <T> value 类型
     */
    <T> void put(String key, T value, long expire, TimeUnit unit);

    void clearAllCache();

    void delete(String key);

    void containsKey(String key);

}
