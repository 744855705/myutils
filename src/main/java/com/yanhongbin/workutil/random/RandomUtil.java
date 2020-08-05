package com.yanhongbin.workutil.random;

import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created with IDEA
 * description : 随机数工具类，圆桌随机使用
 *
 * @author ：yanhongbin
 * @date : Created in 2020/6/2 10:53 上午
 */
public class RandomUtil {

    /**
     * 随机从 from 到 to 之间的随机数，需要from 小于 to
     * @param from 开始区间
     * @param to 结束区间
     * @return int
     */
    private static Integer random(int from, int to) {
        return (int)(Math.random() * (to - from)) + from;
    }

    /**
     * 随机从 0 到 to 之间的随机数
     * @param to
     * @return int
     */
    public static Integer random(int to) {
        return random(0, to);
    }

    /**
     * 随机from to 区间内的一个随机数
     * @param from 开始区间
     * @param to 结束区间
     * @return from 到 to 之间的一个随机数字
     */
    public static Integer randomFromTo(int from, int to){
        if (to > from) {
            return random(from, to);
        } else if (to == from) {
            return from;
        } else {
            return random(to, from);
        }

    }

    /**
     * 随机UUID
     * @return uuid
     */
    public static String getRandomUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 圆桌随机，单个
     * @param probabilities 参与随机
     * @return markCode
     */
    public static String getOneIRandomProbability(Collection<? extends IRandomProbability> probabilities) {
        if (CollectionUtils.isEmpty(probabilities)) {
            return null;
        }

        if (probabilities.size() == 1) {
            return probabilities.iterator().next().getMarkCode();
        }

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int all = 0;

        for (IRandomProbability probability : probabilities) {
            all += probability.getPercentage();
            map.put(probability.getMarkCode(), all);
        }

        Integer random = random(all);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Integer value = entry.getValue();
            if (random < value) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 圆桌随机 多个
     * @param probabilities 参与随机
     * @param num 个数
     * @return markCode String
     */
    public static List<String> getOneIRandomProbability(Collection<? extends IRandomProbability> probabilities, int num) {
        if (num == 1) {
            return Collections.singletonList(getOneIRandomProbability(probabilities));
        }
        if (CollectionUtils.isEmpty(probabilities)) {
            return null;
        }

        if (probabilities.size() == 1) {
            return Collections.singletonList(probabilities.iterator().next().getMarkCode());
        }

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int all = 0;
        for (IRandomProbability probability : probabilities) {
            all += probability.getPercentage();
            map.put(probability.getMarkCode(), all);
        }

        List<String> result = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            Integer random = random(all);
            RANDOM: for (Map.Entry<String, Integer> entry : map.entrySet()) {
                Integer value = entry.getValue();
                if (random < value) {
                    result.add(entry.getKey());
                    break RANDOM;
                }
            }
        }

        return result;
    }

    /**
     * 构建参与随机的对象
     * @param percentage 百分比
     * @param markCode 标记code
     * @return IRandomProbability
     */
    public static IRandomProbability makeIRandomProbability(int percentage, String markCode){
        return new IRandomProbability() {
            @Override
            public Integer getPercentage() {
                return percentage;
            }
            @Override
            public String getMarkCode() {
                return markCode;
            }
        };
    }
}
