package com.yanhongbin.workutil.cache.localcache;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IDEA
 * description : 自定义 Scheduled ThreadFactory
 *
 * @author ：yanhongbin
 * @date : Created in 2020/4/21 9:51 上午
 */
public class ScheduledThreadFactory implements ThreadFactory {
    private final AtomicInteger integer = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(Thread.currentThread().getThreadGroup(), r, "Scheduled-Thread" + integer.incrementAndGet());
    }
}
