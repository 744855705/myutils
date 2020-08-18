package com.yanhongbin.workutil.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IDEA
 * description : {@link ScheduledExecutorService} 代理类
 *              用于延迟任务执行、循环延迟任务的执行
 *
 * @author ：yanhongbin
 * @date : Created in 2020/5/29 10:55 上午
 */
public class ScheduledExecutorProxy {
    private static final ScheduledExecutorService scheduledExecutorService =
        new ScheduledThreadPoolExecutor(4, new ScheduledThreadFactory());

    private static final Logger log = LoggerFactory.getLogger(ScheduledExecutorProxy.class);
    public static ScheduledExecutorService getCacheScheduledExecutorService(){
        return scheduledExecutorService;
    }

    static {
        // jvm关闭的时候关闭线程池，因为jvm关闭会销毁内存，所以可以直接使用 shutdownNow 方式关闭
        // jdk 1.8之后使用
//        Runtime.getRuntime().addShutdownHook(new Thread(scheduledExecutorService::shutdownNow));

        // jdk 1.7之前使用
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("ScheduledExecutorService 线程池 关闭");
                scheduledExecutorService.shutdown();
            }
        }));
    }

    /**
     * 提交循环延迟任务到线程池中
     * @param r runnable
     * @param initialDelay 首次执行的延迟时间
     * @param delay 一次执行终止 与 下一次执行开始之间的延迟
     * @param unit 时间单位
     */
    public static void scheduleWithFixedDelay(Runnable r, long initialDelay, long delay, TimeUnit unit) {
        scheduledExecutorService.scheduleWithFixedDelay(r, initialDelay, delay, unit);
    }

    /**
     * 提交循环延迟任务到线程池中
     * @param r runnable
     * @param initialDelay 首次执行的延迟时间 单位秒
     * @param delay 一次执行终止 与 下一次执行开始之间的延迟 单位秒
     */
    public static void scheduleWithFixedDelaySeconds(Runnable r, long initialDelay, long delay) {
        scheduleWithFixedDelay(r, initialDelay, delay, TimeUnit.SECONDS);
    }

    /**
     * 提交循环延迟任务到线程池中
     * @param r runnable
     * @param initialDelay 首次执行的延迟时间 单位分钟
     * @param delay 一次执行终止 与 下一次执行开始之间的延迟 单位分钟
     */
    public static void scheduleWithFixedDelayMinutes(Runnable r, long initialDelay, long delay) {
        scheduleWithFixedDelay(r, initialDelay, delay, TimeUnit.MINUTES);
    }

    /**
     * 提交循环延迟任务到线程池中
     * @param r runnable
     * @param initialDelay 首次执行的延迟时间 单位小时
     * @param delay 一次执行终止 与 下一次执行开始之间的延迟 单位小时
     */
    public static void scheduleWithFixedDelayHours(Runnable r, long initialDelay, long delay) {
        scheduleWithFixedDelay(r, initialDelay, delay, TimeUnit.HOURS);
    }

    /**
     * 提交循环延迟任务到线程池中
     * @param r runnable
     * @param initialDelay 首次执行的延迟时间 单位天
     * @param delay 一次执行终止 与 下一次执行开始之间的延迟 单位天
     */
    public static void scheduleWithFixedDelayDays(Runnable r, long initialDelay, long delay) {
        scheduleWithFixedDelay(r, initialDelay, delay, TimeUnit.DAYS);
    }

    /**
     * 提交循环延迟任务到线程池，5秒后执行，每5秒执行一次
     * @param r runnable
     */
    public static void scheduleWithFixedDelayFiveSeconds(Runnable r) {
        scheduleWithFixedDelaySeconds(r, 5, 5);
    }

    /**
     * {@link ScheduledExecutorProxy#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
     * @param scheduledCycleRunnableService 任务对象
     */
    public static void scheduleWithFixedDelay(ScheduledCycleRunnableService scheduledCycleRunnableService) {
        scheduleWithFixedDelay(
            scheduledCycleRunnableService,
            scheduledCycleRunnableService.getInitialDelay(),
            scheduledCycleRunnableService.getDelay(),
            scheduledCycleRunnableService.getTimeUnit()
        );
    }

    /**
     * 提交单次延迟任务到线程池
     * @param r run
     * @param initialDelay 延迟时间
     * @param unit 时间单位
     */
    public static void schedule(Runnable r, long initialDelay, TimeUnit unit) {
        scheduledExecutorService.schedule(r, initialDelay, unit);
    }

    /**
     * 提交单次延迟任务到线程池{@link ScheduledExecutorProxy#schedule(Runnable, long, TimeUnit)}
     *
     * @param scheduledOnceRunnableService param
     */
    public static void schedule(ScheduledOnceRunnableService scheduledOnceRunnableService) {
        schedule(
            scheduledOnceRunnableService,
            scheduledOnceRunnableService.getInitialDelay(),
            scheduledOnceRunnableService.getTimeUnit()
        );
    }

    /**
     * 提交单次延迟任务到线程池
     * @param r run
     * @param initialDelay 延迟时间，单位秒
     */
    public static void schedule(Runnable r, long initialDelay) {
        schedule(r, initialDelay, TimeUnit.SECONDS);
    }


    public static void shutdown(){
        scheduledExecutorService.shutdown();
    }


    /**
     * 自定义 Scheduled ThreadFactory
     */
    static class ScheduledThreadFactory implements ThreadFactory {

        private final AtomicInteger integer = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(Thread.currentThread().getThreadGroup(), r, "Scheduled-Thread" + integer.incrementAndGet());
        }
    }

}
