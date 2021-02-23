package com.yanhongbin.workutil.cache.localcache;


import com.yanhongbin.workutil.cache.IRefresh;
import com.yanhongbin.workutil.cache.localcache.node.Node;
import com.yanhongbin.workutil.log.LogUtil;
import com.yanhongbin.workutil.scheduled.ScheduledExecutorProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IDEA
 * description : CacheValueUtil，缓存工具类，根据value判断过期时间
 *
 * FIXME 为了实现自动过期使用了可排序队列 {@link PriorityQueue},存在空间浪费的问题
 *
 * @author ：yanhongbin
 * @date : Created in 2020/4/21 9:51 上午
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class CacheUtil {

    /**
     * 使用ConcurrentHashMap作为缓存，put 线程安全
     */
    private static final ConcurrentHashMap<String, Node> CACHE = new ConcurrentHashMap<String, Node>(1 << 3);

    /**
     * 操作 expireQueue 时需上锁
     */
    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * 用来计算缓存过期的工具队列，线程不安全，操作的时候需要上锁
     */
    @SuppressWarnings("rawtypes")
    private static final PriorityQueue<Node> EXPIRE_QUEUE = new PriorityQueue<Node>(2<<10);

    static {
        ScheduledExecutorProxy.scheduleWithFixedDelayFiveSeconds(new RemoveOverTimeNode());
    }


    /**
     * 放入缓存，无过期时间
     *
     * @param key   key
     * @param value 放入的内容
     * @param <T>   泛型
     */
    public static <T> void put(String key, T value) {
        put(key, value, -1L);
    }

    /**
     * 放入缓存,如果已存在，刷新过期时间
     *
     * @param key    key
     * @param value  放入的内容
     * @param expire 过期时间
     * @param <T>    泛型
     */
    public static <T> void put(String key, T value, long expire) {
        Node<T> newNode = expire == -1L ? new Node<T>(key, value) : new Node<T>(key, value, expire);
        Node oldNode = CACHE.put(key, newNode);
        LOCK.lock();
        try {
            EXPIRE_QUEUE.add(newNode);
            if (oldNode != null) {
                EXPIRE_QUEUE.remove(oldNode);
            }
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 刷新缓存时间，按照当前时间和生成缓存时的过期时间刷新
     *
     * @param key key
     */
    public static void refreshExpireTime(String key) {
        Node node = CACHE.get(key);
        if (node == null) {
            return;
        }
        node.refreshExpireTime();
    }

    /**
     * 刷新过期时间
     *
     * @param key    key
     * @param expire expire 秒后过期
     */
    public static void refreshExpireTime(String key, long expire) {
        Node node = CACHE.get(key);
        if (node == null) {
            return;
        }
        node.refreshExpireTime(expire);
    }

    /**
     * 获取缓存内的内容，没有和已过期返回null（获取不会删除缓存）
     *
     * @param <T> 泛型
     * @param key key
     * @return value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Node<T> node = CACHE.get(key);
        return node != null && node.checkAndRemoveOverTime() ? node.getValue() : null;
    }

    /**
     * 删除缓存内容
     *
     * @param key key
     */
    public static void delete(String key) {
        LOCK.lock();
        try {
            Node remove = CACHE.remove(key);
            EXPIRE_QUEUE.remove(remove);
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 清空缓存
     */
    public static void clear() {
        LOCK.lock();
        try {
            LogUtil.info(log, "清空缓存");
            CACHE.clear();
            EXPIRE_QUEUE.clear();
        } finally {
            LOCK.unlock();
        }
    }


    /**
     * 从缓存中删除过期的key
     */
    public static void clearOverTimeNode() {
        LOCK.lock();
        try {
            while (true) {
                Node peek = EXPIRE_QUEUE.peek();
                if (peek == null) {
                    return;
                }
                boolean check = peek.check();
                if (!check) {
                    // 已过期
                    CACHE.remove(peek.getKey());
                    EXPIRE_QUEUE.poll();
                } else {
                    return;
                }
            }
        } finally {
            LOCK.unlock();
        }
    }


    /**
     * 获取缓存内容，如果缓存中没有，则调用{@link IRefresh#getContent()} 方法，获取内容并放入缓存
     * @param key key
     * @param refresh 刷新方法
     * @param <T> 缓存类型
     * @return 缓存内容
     */
    public static <T> T getAndRefresh(String key,IRefresh<T> refresh){
        T t = get(key);
        // 对象类型处理
        if (t == null) {
            LogUtil.info(log, "未查到本地缓存，key:{}", key);
            synchronized (key.intern()) {
                t = get(key);
                if (t == null) {
                    t = refreshContent(key, refresh);
                }
            }
            return t;
        }
        if (t instanceof String) {
            return refreshString(key, refresh, t);
        }

        // 集合类型处理
        if (t instanceof Collection) {
            return refreshCollection(key, refresh, t);
        }
        // Map 类型处理
        if (t instanceof Map) {
            return refreshMap(key, refresh, t);
        }

        return t;
    }

    private static <T> T refreshString(String key, IRefresh<T> refresh, T t) {
        if (isEmptyOrBlank((String) t)) {
            LogUtil.info(log, "未查到本地缓存，key:{}", key);
            synchronized (key.intern()) {
                t = get(key);
                if (isEmptyOrBlank((String) t)) {
                    t = refreshContent(key, refresh);
                }
            }
        }
        return t;
    }

    private static boolean isEmptyOrBlank(String t) {
        return t == null || t.length() == 0 || t.trim().length() == 0 || "null".equals(t) || "NULL".equals(t) ;
    }

    /**
     * 刷新map类型数据
     * @param key key
     * @param refresh 缓存刷新方法
     * @param t 要刷新的对象
     * @param <T> 对象类型
     * @return 刷新后的对象
     */
    private static <T> T refreshMap(String key, IRefresh<T> refresh, T t) {
        if (((Map) t).isEmpty()) {
            LogUtil.info(log, "未查到本地缓存，key:{}", key);
            synchronized (key.intern()) {
                t = get(key);
                if (t == null || ((Map) t).isEmpty()) {
                    t = refreshContent(key, refresh);
                }
            }
        }
        return t;
    }

    private static <T> T refreshCollection(String key, IRefresh<T> refresh, T t) {
        if (((Collection) t).isEmpty()) {
            LogUtil.info(log, "未查到本地缓存，key:{}", key);
            synchronized (key.intern()) {
                t = get(key);
                if ( t ==null || ((Collection) t).isEmpty()) {
                    t = refreshContent(key, refresh);
                }
            }
        }
        return t;
    }

    private static <T> T refreshContent(String key, IRefresh<T> refresh) {
        LogUtil.info(log, "刷新缓存,key:{}", key);
        T t = refresh.getContent();
        put(key, t, refresh.getExpire());
        return t;
    }
}