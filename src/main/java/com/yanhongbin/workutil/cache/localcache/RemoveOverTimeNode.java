package com.yanhongbin.workutil.cache.localcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/18 11:30 上午
 */
public class RemoveOverTimeNode implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RemoveOverTimeNode.class);


    @Override
    public void run() {
        log.info("清除处理过期缓存");
        LocalCacheManager.getCacheManager().clearOverTimeNode();
    }
}
