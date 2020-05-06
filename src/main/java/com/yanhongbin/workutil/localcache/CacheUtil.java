package com.yanhongbin.workutil.localcache;


import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created with IDEA, JDK1.8以上
 * description : CacheValueUtil，缓存工具类，根据value判断过期时间
 *
 * @author ：yanhongbin
 * @date : Created in 2020/4/21 9:51 上午
 */
public class CacheUtil {

    /**
     * 使用ConcurrentHashMap作为缓存，put 线程安全
     */
    private static final ConcurrentHashMap<String, Node> cache = new ConcurrentHashMap<String, Node>(1<<3);

    /**
     * 操作 expireQueue 时需上锁
     */
    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * 用来计算缓存过期的工具队列，线程不安全
     */
    private static final PriorityQueue<Node> expireQueue = new PriorityQueue<>(1024);

    /**
     * 清除过期缓存的线程池
     */
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new ScheduledThreadFactory());

    static {
        scheduledExecutorService.scheduleWithFixedDelay(new RemoveOverTimeNode(), 5, 5, TimeUnit.SECONDS);
        // 1.8以后使用
        Runtime.getRuntime().addShutdownHook(new Thread(scheduledExecutorService::shutdownNow));

        // 1.7以前使用
//        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//            @Override
//            public void run() {
//                scheduledExecutorService.shutdownNow();
//            }
//        }));
    }



    /**
     * 放入缓存，无过期时间
     * @param key key
     * @param value 放入的内容
     * @param <T> 泛型
     */
    public static <T> void put(String key, T value) {
        put(key, value, -1L);
    }

    /**
     * 放入缓存
     * @param key key
     * @param value 放入的内容
     * @param expire 过期时间
     * @param <T> 泛型
     */
    public static <T> void put(String key, T value, long expire) {
        Node<T> newNode = expire == -1L ? new Node<T>(key, value) : new Node<T>(key, value, expire);
        Node oldNode = cache.put(key, newNode);
        lock.lock();
        try {
            expireQueue.add(newNode);
            if (oldNode == null) {
                expireQueue.remove(oldNode);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取缓存内的内容，没有和已过期返回null（获取不会删除缓存）
     * @param key key
     * @param <T> 泛型
     * @return value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Node<T> node = cache.get(key);
        return node != null && node.checkAndRemoveOverTime() ? node.getValue() : null;
    }

    /**
     * 删除缓存内容
     * @param key key
     */
    public static void delete(String key) {
        lock.lock();
        try {
            Node remove = cache.remove(key);
            expireQueue.remove(remove);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 清空缓存
     */
    public static void clear(){
        lock.lock();
        try {
            cache.clear();
            expireQueue.clear();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 删除过期的key，FIXME 过大的时候会缓慢，谨慎调用
     * @deprecated 使用 {@link CacheUtil#clearOverTimeNode()}
     */
    @Deprecated
    public static void clearAllOverTimeNode() {
        lock.lock();
        try {
            cache.forEach((key, node)->{
                node.checkAndRemoveOverTime();
            });
        }finally {
            lock.unlock();
        }

    }

    /**
     * 从缓存中删除过期的key
     */
    public static void clearOverTimeNode() {
        lock.lock();
        try {
            System.out.println("队列大小："+expireQueue.size());
            while (true) {
                Node peek = expireQueue.peek();
                if (peek == null) {
                    return;
                }
                boolean check = peek.check();
                if (!check) {
                    // 已过期
                    cache.remove(peek.getKey());
                    System.out.println("删除:" + peek.getKey());
                    expireQueue.poll();
                }else {
                    return;
                }
            }
        }finally {
            lock.unlock();
        }
    }
}

