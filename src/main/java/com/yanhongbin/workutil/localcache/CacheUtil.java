package com.yanhongbin.workutil.localcache;


import com.yanhongbin.workutil.scheduled.ScheduledExecutorProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.PriorityQueue;
import java.util.concurrent.*;
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
public class CacheUtil {

    private static final Logger log = LoggerFactory.getLogger(CacheUtil.class);


    /**
     * 使用ConcurrentHashMap作为缓存，put 线程安全
     */
    private static final ConcurrentHashMap<String, Node> cache = new ConcurrentHashMap<String, Node>(1 << 3);

    /**
     * 操作 expireQueue 时需上锁
     */
    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * 用来计算缓存过期的工具队列，线程不安全
     */
    @SuppressWarnings("rawtypes")
    private static final PriorityQueue<Node> expireQueue = new PriorityQueue<Node>(1024);

    static {
        ScheduledExecutorProxy.scheduleWithFixedDelayFiveSeconds(new ScheduledCycleRunnableServiceRemoveOverTimeNodeImpl());
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
        Node oldNode = cache.put(key, newNode);
        lock.lock();
        try {
            expireQueue.add(newNode);
            if (oldNode != null) {
                expireQueue.remove(oldNode);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 刷新缓存时间，按照当前时间和生成缓存时的过期时间刷新
     *
     * @param key key
     */
    public static void refreshExpireTime(String key) {
        Node node = cache.get(key);
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
        Node node = cache.get(key);
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
        Node<T> node = cache.get(key);
        return node != null && node.checkAndRemoveOverTime() ? node.getValue() : null;
    }

    /**
     * 删除缓存内容
     *
     * @param key key
     */
    public static void delete(String key) {
        lock.lock();
        try {
            Node remove = cache.remove(key);
            expireQueue.remove(remove);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 清空缓存
     */
    public static void clear() {
        lock.lock();
        try {
            log.info("清空缓存");
            cache.clear();
            expireQueue.clear();
        } finally {
            lock.unlock();
        }
    }

//    /**
//     * 删除过期的key，FIXME 过大的时候会缓慢，谨慎调用
//     * @deprecated 使用 {@link CacheUtil#clearOverTimeNode()}
//     */
//    public static void clearAllOverTimeNode() {
//        lock.lock();
//        try {
//            cache.forEach((key, node)->{
//                node.checkAndRemoveOverTime();
//            });
//        }finally {
//            lock.unlock();
//        }
//    }

    /**
     * 从缓存中删除过期的key
     */
    public static void clearOverTimeNode() {
        lock.lock();
        try {
            while (true) {
                Node peek = expireQueue.peek();
                if (peek == null) {
                    return;
                }
                boolean check = peek.check();
                if (!check) {
                    // 已过期
                    cache.remove(peek.getKey());
                    expireQueue.poll();
                } else {
                    return;
                }
            }
        } finally {
            lock.unlock();
        }
    }
}