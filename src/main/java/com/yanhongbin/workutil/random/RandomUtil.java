package com.yanhongbin.workutil.random;

import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created with IDEA
 * description : ����������࣬Բ�����ʹ��
 *
 * @author ��yanhongbin
 * @date : Created in 2020/6/2 10:53 ����
 */
public class RandomUtil {

    /**
     * ����� from �� to ֮������������Ҫfrom С�� to
     * @param from ��ʼ����
     * @param to ��������
     * @return int
     */
    private static Integer random(int from, int to) {
        return (int)(Math.random() * (to - from)) + from;
    }

    /**
     * ����� 0 �� to ֮��������
     * @param to
     * @return int
     */
    public static Integer random(int to) {
        return random(0, to);
    }

    /**
     * ���from to �����ڵ�һ�������
     * @param from ��ʼ����
     * @param to ��������
     * @return from �� to ֮���һ���������
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
     * ���UUID
     * @return uuid
     */
    public static String getRandomUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * Բ�����������
     * @param probabilities �������
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
     * Բ����� ���
     * @param probabilities �������
     * @param num ����
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
     * ������������Ķ���
     * @param percentage �ٷֱ�
     * @param markCode ���code
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
