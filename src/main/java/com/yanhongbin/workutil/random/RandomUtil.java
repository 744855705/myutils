package com.yanhongbin.workutil.random;

import com.yanhongbin.workutil.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IDEA
 * description : 随机数工具类，圆桌随机使用
 *
 * @author ：yanhongbin
 * @date : Created in 2020/6/2 10:53 上午
 */
public class RandomUtil {

    private static final Logger log = LoggerFactory.getLogger(RandomUtil.class);

    /**
     * 随机从 from 到 to 之间的随机数
     * @param from 开始区间
     * @param to 结束区间
     * @return int
     */
    public static int random(int from, int to) {
        if (from == to) {
            return from;
        }
        return (int)(Math.random() * (to - from)) + from;
    }

    /**
     * 随机从 0 到 to 之间的随机数
     * @param to
     * @return int
     */
    public static int random(int to) {
        return random(0, to);
    }

    /**
     * 随机UUID
     * @return
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
    public static String getIRandomProbability(Collection<? extends IRandomProbability> probabilities) {
        List<String> iRandomProbability = getIRandomProbability(probabilities, 1);
        if (CollectionUtils.isEmpty(iRandomProbability)) {
            return null;
        }
        return iRandomProbability.get(0);
    }

    /**
     * 圆桌随机 多个
     *
     * @param probabilities 参与随机
     * @param num           个数
     * @return markCode String
     */
    public static List<String> getIRandomProbability(Collection<? extends IRandomProbability> probabilities, int num) {
        log.info("随机结果个数：{}", num);
        if (CollectionUtils.isEmpty(probabilities)) {
            return Collections.emptyList();
        }
        List<RandomProbabilityTemp> list = new LinkedList<>();
        int all = getAll(probabilities, list);
        log.info("概率相加总数：{}", all);
        List<String> result = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            int random = random(all);
            log.info("本次随机数：{}", random);
            for (RandomProbabilityTemp randomProbabilityTemp : list) {
                if (randomProbabilityTemp.match(random)) {
                    log.info("命中：{}", randomProbabilityTemp);
                    result.add(randomProbabilityTemp.getMarkCode());
                    break;
                }
            }
        }
        return result;
    }

    private static int getAll(Collection<? extends IRandomProbability> probabilities, List<RandomProbabilityTemp> list) {
        Iterator<? extends IRandomProbability> iterator = probabilities.iterator();
        int pre = 0;
        int all = 0;
        while (iterator.hasNext()) {
            IRandomProbability probability = iterator.next();
            Integer percentage = probability.getPercentage();
            if (percentage == 0) {
                iterator.remove();
                continue;
            }
            all += probability.getPercentage();
            list.add(new RandomProbabilityTemp(pre, probability.getMarkCode(), all));
            pre = all;
        }
        return all;
    }

    /**
     * 构建参与随机的对象
     * @param percentage 百分比
     * @param markCode 标记code
     * @return IRandomProbability
     */
    public static IRandomProbability makeRandomProbability(int percentage, String markCode){
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


    /**
     * 匹配使用内部类
     */
    static class RandomProbabilityTemp {
        private final int pre;
        private final String markCode;
        private final int probability;

        public RandomProbabilityTemp(int pre, String markCode, int probability) {
            this.pre = pre;
            this.markCode = markCode;
            this.probability = probability;
        }

        public String getMarkCode() {
            return markCode;
        }

        /**
         * 匹配
         */
        public boolean match(int random) {
            return random >= this.pre && random < this.probability;
        }

        @Override
        public String toString() {
            return "RandomProbability{" +
                "pre=" + pre +
                ", markCode='" + markCode + '\'' +
                ", probability=" + probability +
                '}';
        }
    }

}
