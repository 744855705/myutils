package com.yanhongbin.workutil.localcache;


/**
 * Created with IDEA
 * description : 缓存内封装value节点信息，用来判断是否过期
 *
 * @author ：yanhongbin
 * @date : Created in 2020/4/21 9:51 上午
 */
public class Node<T> implements Comparable<Node>{

    private String key;
    /**
     * 创建时间，毫秒时间戳
     */
    private Long createTime;
    /**
     * 要保存的值
     */
    private T value;
    /**
     * 过期时间，单位秒
     */
    private Long expire;

    public Node(String key,T value) {
        this(key, value, -1L);
    }

    public Node(String key, T value, Long expire) {
        this.key = key;
        this.value = value;
        this.createTime = System.currentTimeMillis();
        if (expire < 0) {
            this.expire = -1L;
        } else {
            this.expire = expire;
        }
    }

    /**
     * 刷新过期时间，从当前时刻开始重新计时，过期时间为之前传入的expire，单位秒
     */
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
    public void refreshExpireTime(Long expire) {
        this.expire = expire;
        this.refreshExpireTime();
    }
    /**
     * 检查是否过期
     * @return true 标识未过期 false 标识已过期
     */
    public boolean check(){
        if (expire == -1) {
            return true;
        } else {
            return (System.currentTimeMillis() - this.createTime) / 1000 < this.expire;
        }
    }

    /**
     * 检查是否过期,如果过期，在缓存中清除
     * @return true 标识未过期 false 标识已过期
     */
    public boolean checkAndRemoveOverTime() {
        if (check()) {
            // 未过期
            return true;
        } else {
            CacheUtil.delete(this.key);
            return false;
        }
    }

    /**
     * 获取到期时间
     * @return 还有多长时间到期
     */
    public long getExpireTime(){
        return expire - ((System.currentTimeMillis() - this.createTime) / 1000);
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(Node o) {

        if (this.expire == -1) {
            return 1;
        }
        if (o.getExpire() == -1) {
            return -1;
        }

        long r = this.getExpireTime() - o.getExpireTime();
        if (r > 0) {
            return 1;
        }
        if (r < 0) {
            return -1;
        }
        return 0;
    }
}
