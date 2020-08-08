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
 * description : {@link ScheduledExecutorService} ������
 *              �����ӳ�����ִ�С�ѭ���ӳ������ִ��
 *
 * @author ��yanhongbin
 * @date : Created in 2020/5/29 10:55 ����
 */
public class ScheduledExecutorProxy {
    private static final ScheduledExecutorService scheduledExecutorService =
        new ScheduledThreadPoolExecutor(4, new ScheduledThreadFactory());

    private static final Logger log = LoggerFactory.getLogger(ScheduledExecutorProxy.class);
    public static ScheduledExecutorService getCacheScheduledExecutorService(){
        return scheduledExecutorService;
    }

    static {
        // jvm�رյ�ʱ��ر��̳߳أ���Ϊjvm�رջ������ڴ棬���Կ���ֱ��ʹ�� shutdownNow ��ʽ�ر�
        // jdk 1.8֮��ʹ��
//        Runtime.getRuntime().addShutdownHook(new Thread(scheduledExecutorService::shutdownNow));

        // jdk 1.7֮ǰʹ��
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("ScheduledExecutorService �̳߳� �ر�");
                scheduledExecutorService.shutdown();
            }
        }));
    }

    /**
     * �ύѭ���ӳ������̳߳���
     * @param r runnable
     * @param initialDelay �״�ִ�е��ӳ�ʱ��
     * @param delay һ��ִ����ֹ �� ��һ��ִ�п�ʼ֮����ӳ�
     * @param unit ʱ�䵥λ
     */
    public static void scheduleWithFixedDelay(Runnable r, long initialDelay, long delay, TimeUnit unit) {
        scheduledExecutorService.scheduleWithFixedDelay(r, initialDelay, delay, unit);
    }

    /**
     * �ύѭ���ӳ������̳߳���
     * @param r runnable
     * @param initialDelay �״�ִ�е��ӳ�ʱ�� ��λ��
     * @param delay һ��ִ����ֹ �� ��һ��ִ�п�ʼ֮����ӳ� ��λ��
     */
    public static void scheduleWithFixedDelaySeconds(Runnable r, long initialDelay, long delay) {
        scheduleWithFixedDelay(r, initialDelay, delay, TimeUnit.SECONDS);
    }

    /**
     * �ύѭ���ӳ������̳߳���
     * @param r runnable
     * @param initialDelay �״�ִ�е��ӳ�ʱ�� ��λ����
     * @param delay һ��ִ����ֹ �� ��һ��ִ�п�ʼ֮����ӳ� ��λ����
     */
    public static void scheduleWithFixedDelayMinutes(Runnable r, long initialDelay, long delay) {
        scheduleWithFixedDelay(r, initialDelay, delay, TimeUnit.MINUTES);
    }

    /**
     * �ύѭ���ӳ������̳߳���
     * @param r runnable
     * @param initialDelay �״�ִ�е��ӳ�ʱ�� ��λСʱ
     * @param delay һ��ִ����ֹ �� ��һ��ִ�п�ʼ֮����ӳ� ��λСʱ
     */
    public static void scheduleWithFixedDelayHours(Runnable r, long initialDelay, long delay) {
        scheduleWithFixedDelay(r, initialDelay, delay, TimeUnit.HOURS);
    }

    /**
     * �ύѭ���ӳ������̳߳���
     * @param r runnable
     * @param initialDelay �״�ִ�е��ӳ�ʱ�� ��λ��
     * @param delay һ��ִ����ֹ �� ��һ��ִ�п�ʼ֮����ӳ� ��λ��
     */
    public static void scheduleWithFixedDelayDays(Runnable r, long initialDelay, long delay) {
        scheduleWithFixedDelay(r, initialDelay, delay, TimeUnit.DAYS);
    }

    /**
     * �ύѭ���ӳ������̳߳أ�5���ִ�У�ÿ5��ִ��һ��
     * @param r runnable
     */
    public static void scheduleWithFixedDelayFiveSeconds(Runnable r) {
        scheduleWithFixedDelaySeconds(r, 5, 5);
    }

    /**
     * {@link ScheduledExecutorProxy#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
     * @param scheduledCycleRunnableService �������
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
     * �ύ�����ӳ������̳߳�
     * @param r run
     * @param initialDelay �ӳ�ʱ��
     * @param unit ʱ�䵥λ
     */
    public static void schedule(Runnable r, long initialDelay, TimeUnit unit) {
        scheduledExecutorService.schedule(r, initialDelay, unit);
    }

    /**
     * �ύ�����ӳ������̳߳�{@link ScheduledExecutorProxy#schedule(Runnable, long, TimeUnit)}
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
     * �ύ�����ӳ������̳߳�
     * @param r run
     * @param initialDelay �ӳ�ʱ�䣬��λ��
     */
    public static void schedule(Runnable r, long initialDelay) {
        schedule(r, initialDelay, TimeUnit.SECONDS);
    }





    /**
     * �Զ��� Scheduled ThreadFactory
     */
    static class ScheduledThreadFactory implements ThreadFactory {

        private final AtomicInteger integer = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(Thread.currentThread().getThreadGroup(), r, "Scheduled-Thread" + integer.incrementAndGet());
        }
    }

}
