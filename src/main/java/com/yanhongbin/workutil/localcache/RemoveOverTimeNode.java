package com.yanhongbin.workutil.localcache;


/**
 * Created with IDEA
 * description : 清除过期缓存Runnable
 *
 * @author ：yanhongbin
 * @date : Created in 2020/4/21 9:51 上午
 */
public class RemoveOverTimeNode implements Runnable{

    @Override
    public void run() {
        CacheUtil.clearOverTimeNode();
    }
}
