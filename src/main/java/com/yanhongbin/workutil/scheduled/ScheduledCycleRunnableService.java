package com.yanhongbin.workutil.scheduled;

/**
 * Created with IDEA
 * description : ѭ���ӳ�����
 *
 * @author ��yanhongbin
 * @date : Created in 2020/5/29 11:43 ����
 */
public interface ScheduledCycleRunnableService extends ScheduledOnceRunnableService {


    /**
     * һ��ִ����ֹ �� ��һ��ִ�п�ʼ֮����ӳ�
     *
     * @return int
     */
    Integer getDelay();

}
