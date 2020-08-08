package com.yanhongbin.workutil.scheduled;

import java.util.concurrent.TimeUnit;

/**
 * Created with IDEA
 * description : �����ӳ�����
 *
 * @author ��yanhongbin
 * @date : Created in 2020/6/28 2:19 ����
 */
public interface ScheduledOnceRunnableService extends Runnable {

    /**
     * ִ�е��ӳ�ʱ��
     *
     * @return int
     */
    Integer getInitialDelay();


    /**
     * ʱ�䵥λ
     *
     * @return TimeUnit
     */
    TimeUnit getTimeUnit();
}
