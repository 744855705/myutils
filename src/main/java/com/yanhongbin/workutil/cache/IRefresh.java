package com.yanhongbin.workutil.cache;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/15 10:41 上午
 */
public interface IRefresh<T> {
    /**
     * 获取内容，用于存入缓存
     * @return T
     */
    T getContent();

    /**
     * 缓存过期时间，不重写该方法则默认 -1
     * @return
     */
    default int getExpire(){
        return -1;
    }
}
