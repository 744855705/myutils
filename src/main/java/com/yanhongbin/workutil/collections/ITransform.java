package com.yanhongbin.workutil.collections;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/8 11:35 上午
 */
public interface ITransform<M, N> {
    /**
     * 类型转换
     * @param n 要被转换的类型
     * @return 转换后
     */
    N transform(M n);
}
