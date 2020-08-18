package com.yanhongbin.workutil.scheduled;

import java.util.concurrent.TimeUnit;

/**
 * Created with IDEA
 * description : 单次延迟任务
 *
 * @author ：yanhongbin
 * @date : Created in 2020/6/28 2:19 下午
 */
public interface ScheduledOnceRunnableService extends Runnable {

    /**
     * 执行的延迟时间
     *
     * @return int
     */
    Integer getInitialDelay();


    /**
     * 时间单位
     *
     * @return TimeUnit
     */
    TimeUnit getTimeUnit();
}
