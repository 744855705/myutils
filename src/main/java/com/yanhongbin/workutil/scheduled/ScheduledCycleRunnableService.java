package com.yanhongbin.workutil.scheduled;

/**
 * Created with IDEA
 * description : 循环延迟任务
 *
 * @author ：yanhongbin
 * @date : Created in 2020/5/29 11:43 上午
 */
public interface ScheduledCycleRunnableService extends ScheduledOnceRunnableService {


    /**
     * 一次执行终止 与 下一次执行开始之间的延迟
     *
     * @return int
     */
    Integer getDelay();

}
