package com.yanhongbin.workutil.random;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/5 3:10 下午
 */
public interface IRandomProbability {

    /**
     * 获取占比
     * @return percentage
     */
    Integer getPercentage();

    /**
     * 获取标记字段
     * @return markCode
     */
    String getMarkCode();
}
