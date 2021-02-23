package com.yanhongbin.workutil.cache.localcache.node;


import com.yanhongbin.workutil.cache.localcache.CacheUtil;

/**
 * Created with IDEA
 * description : 缓存内封装value节点信息，用来判断是否过期
 *
 * @author ：yanhongbin
 * @date : Created in 2020/4/21 9:51 上午
 */
public class Node<T> extends BaseOverTime implements Comparable<Node<T>> {

    private String key;
    /**
     * 要保存的值
     */
    private T value;


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

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
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
        if (r > Integer.MAX_VALUE) {
            return 1;
        }
        if (r < Integer.MIN_VALUE) {
            return -1;
        }
        return (int)r;
    }


}
