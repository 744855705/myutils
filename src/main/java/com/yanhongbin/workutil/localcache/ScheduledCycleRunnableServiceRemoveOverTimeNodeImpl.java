package com.yanhongbin.workutil.localcache;


import com.yanhongbin.workutil.scheduled.ScheduledCycleRunnableService;

import java.util.concurrent.TimeUnit;

/**
 * Created with IDEA
 * description : 清除过期缓存Runnable
 *
 * @author ：yanhongbin
 * @date : Created in 2020/4/21 9:51 上午
 */
public class ScheduledCycleRunnableServiceRemoveOverTimeNodeImpl implements ScheduledCycleRunnableService {

    @Override
    public void run() {
        CacheUtil.clearOverTimeNode();
    }

    @Override
    public Integer getDelay() {
        return 5;
    }

    @Override
    public Integer getInitialDelay() {
        return 5;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }
}
