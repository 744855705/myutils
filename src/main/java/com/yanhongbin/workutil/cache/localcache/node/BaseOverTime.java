package com.yanhongbin.workutil.cache.localcache.node;

import com.yanhongbin.workutil.cache.localcache.CacheUtil;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/16 3:23 下午
 */
public abstract class BaseOverTime implements IOverTime {

    /**
     * 过期时间，单位秒
     */
    protected Long expire;


    /**
     * 创建时间，毫秒时间戳
     */
    protected Long createTime;

    /**
     * 刷新过期时间，从当前时刻开始重新计时，过期时间为之前传入的expire，单位秒
     */
    @Override
    public void refreshExpireTime(){
        if (this.expire == -1) {
            return;
        }
        this.createTime = System.currentTimeMillis();
    }

    /**
     * 重新刷新过期时间，从当前时间开始，{@param expire} 秒后过期
     * @param expire 过期时间，从当前时间开始计时，expire 秒后过期，传入 -1 表示不过期
     */
    @Override
    public void refreshExpireTime(Long expire) {
        this.expire = expire;
        this.refreshExpireTime();
    }
    /**
     * 检查是否过期
     * @return true 标识未过期 false 标识已过期
     */
    @Override
    public boolean check(){
        if (expire == -1) {
            return true;
        } else {
            return (System.currentTimeMillis() - this.createTime) / 1000 < this.expire;
        }
    }

    /**
     * 获取到期时间
     * @return 还有多长时间到期
     */
    @Override
    public long getExpireTime(){
        return expire - ((System.currentTimeMillis() - this.createTime) / 1000);
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
