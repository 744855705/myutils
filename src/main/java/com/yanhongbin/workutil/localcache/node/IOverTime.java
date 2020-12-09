package com.yanhongbin.workutil.localcache.node;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/16 3:16 下午
 */
public interface IOverTime {

    /**
     * 检查是否过期
     * @return true 标识未过期 false 标识已过期
     */
    boolean check();

    /**
     * 获取过期时间
     * @return 还有多长时间到期（秒）
     */
    long getExpireTime();

    /**
     * 刷新过期时间
     */
    void refreshExpireTime();

    /**
     * 刷新为 expire 后过期
     * @param expire 秒
     */
    void refreshExpireTime(Long expire);
}
