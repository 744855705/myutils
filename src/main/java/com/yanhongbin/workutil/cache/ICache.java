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
     * 声明不过期的变量时使用的过期时间占位参数
     */
    Long NOT_EXPIRE = -1L;

    /**
     * 从缓存中获取数据
     *
     * @param key key
     * @param <T> 数据类型
     * @return value
     */
    <T> T get(String key);

    /**
     * 放入缓存
     *
     * @param key   key
     * @param value value
     * @param <T>   value类型
     */
    default <T> void put(String key, T value){
        put(key, value, NOT_EXPIRE);
    }

    /**
     * 放入缓存，声明过期时间
     *
     * @param key    key
     * @param value  value
     * @param expire 过期时间,单位秒
     * @param <T>    value 类型
     */
    <T> void put(String key, T value, long expire);

    /**
     * 放入缓存，声明过期时间,{@link #put(String, Object, long)}
     *
     * @param key    key
     * @param value  value
     * @param expire 过期时间
     * @param unit   过期时间单位
     * @param <T>    value 类型
     */
    default <T> void put(String key, T value, long expire, TimeUnit unit) {
        // 默认实现，将时间转换成秒，调用 #put(String, Object, long) 方法
        put(key, value, TimeUnit.SECONDS.convert(expire, unit));
    }

    /**
     * 清除全部缓存
     */
    void clearAllCache();

    /**
     * 删除某个 key 对应的缓存
     *
     * @param key key
     */
    void delete(String key);

    /**
     * 返回并删除某个key 对应的缓存，若不存在该缓存内容，则返回null
     *
     * @param key key
     * @param clazz class
     * @param <T> value 类型
     * @return T t
     */
    @SuppressWarnings("unchecked")
    default <T> T delete(String key, Class<T> clazz){
        Object o = get(key);
        if (o == null) {
            return null;
        }
        if (isAssignableFrom(o, clazz)) {
            delete(key);
            return (T) o;
        } else {
            throw new ClassCastException("Cannot cast " + o.getClass().getName() + " to " + clazz.getName());
        }
    }

    /**
     * 判断是否包含 key 对应的缓存
     *
     * @param key key
     * @return true 缓存存在 false 缓存不存在
     */
    default boolean containsKey(String key) {
        return get(key) != null;
    }

    /**
     * 清除所有过期内容,默认不实现
     */
    default void clearOverTimeNode(){}

    /**
     * 先查询,当查询不到时,调用{@link IRefresh#getContent()}获取内容,放入缓存并返回
     * @param key key
     * @param refresh 获取内容方法,在缓存中不能获取到内容时调用
     * @param <T> 缓存value 类型
     * @return value
     */
    <T> T getAndRefresh(String key, IRefresh<T> refresh);

    /**
     * 判断 {@code o.getClass()}是否是 {@param clazz}的子类
     *
     * @param o     o
     * @param clazz clazz
     * @return o 和 clazz 均不为null，并且 {@code o.getClass()} 是 {@param clazz} 的子类
     */
    static boolean isAssignableFrom(Object o, Class<?> clazz) {
        if (o == null || clazz == null) {
            return false;
        }
        return clazz.isAssignableFrom(o.getClass());
    }
}
