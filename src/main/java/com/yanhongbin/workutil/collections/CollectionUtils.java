package com.yanhongbin.workutil.collections;


import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/8 11:32 上午
 */
public class CollectionUtils {

    /**
     * List转换
     *
     * @param source    源
     * @param transform 转换方法
     * @param <M>       传入的类型
     * @param <N>       转换后的类型
     * @return List
     */
    public static <M, N> List<N> transformToList(Collection<M> source, ITransform<? super M, ? extends N> transform) {
        return source.stream().map(transform::transform).collect(Collectors.toList());
    }

    /**
     * 转换为set
     *
     * @param source    源
     * @param transform 转换方法
     * @param <M>       传入的类型
     * @param <N>       转换后的类型
     * @return Set
     */
    public static <M, N> Set<N> transformToSet(Collection<M> source, ITransform<? super M, ? extends N> transform) {
        return source.stream().map(transform::transform).collect(Collectors.toSet());
    }

    /**
     * 转换为队列
     *
     * @param source    源
     * @param transform 转换方法
     * @param <M>       传入的类型
     * @param <N>       转换后的类型
     * @return Queue
     */
    public static <M, N> Queue<N> transformToQueue(Collection<M> source, ITransform<? super M, ? extends N> transform) {
        return new LinkedList<>(transformToList(source, transform));
    }

    /**
     * 多线程处理数组循环
     *
     * @param array  array
     * @param action 循环操作方法
     * @param <T>    数组类型
     */
    public static <T> void forEach(T[] array, Consumer<? super T> action) {
        forEach(Arrays.asList(array), action);
    }

    /**
     * 多线程处理 Collection 循环
     *
     * @param collection collection
     * @param action     循环操作方法
     * @param <T>        Collection泛型
     */
    public static <T> void forEach(Collection<T> collection, Consumer<? super T> action) {
        collection.parallelStream().forEach(action);
    }

    /**
     * 判断 collection isEmpty
     *
     * @param collection collection
     * @return null or empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断map是否为空
     *
     * @param map map
     * @return null or empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 创建只有一个元素的List
     * @param t t
     * @param <T> 泛型
     * @return List<T>
     */
    public static <T> List<T> createSingletonList(T t) {
        return Collections.singletonList(t);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> collection, Class<T> clazz) {
        return collection.toArray((T[])Array.newInstance(clazz,collection.size()));
    }

    public static void main(String[] args) {
        String[] strings = toArray(new LinkedList<>(Arrays.asList("1", "2", "3")), String.class);
        for (String string : strings) {
            System.out.println(string);
        }
    }

}
