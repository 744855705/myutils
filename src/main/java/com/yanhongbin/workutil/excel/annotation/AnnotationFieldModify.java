package com.yanhongbin.workutil.excel.annotation;

import com.yanhongbin.workutil.collections.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA
 * description: 动态的修改Annotation 内字段的值
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/23 13:28
 */
public class AnnotationFieldModify {

    /**
     * 修改注解内字段的值
     * @param annotation 注解对象
     * @param map memberValues
     * @throws NoSuchFieldException throws by {@link Field#get}
     * @throws IllegalAccessException throws by {@link Field#set}
     */
    @SuppressWarnings("unchecked")
    public static void modifyAnnotationFieldValue(Annotation annotation, Map<String, Object> map) throws NoSuchFieldException, IllegalAccessException {
        // 取出注解的代理对象
        if (annotation == null) {
            // 注解为null 的情况下不做任何操作
            return;
        }
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Field field = invocationHandler.getClass().getDeclaredField("memberValues");
        field.setAccessible(true);
        Map<String, Object> memberValues = (Map<String, Object>) field.get(invocationHandler);
        memberValues.putAll(map);
        field.set(invocationHandler, memberValues);
    }


    /**
     * 根据Class和Field修改{@link ExcelDictionary}注解的字段值
     *
     * @param clazz clazz
     * @param field fieldName
     * @param map   memberValues
     * @throws NoSuchFieldException   throws by {@link Field#get}
     * @throws IllegalAccessException throws by {@link Field#set}
     */
    private static void modifyAnnotationExcelDictionaryMemberValues(Class<?> clazz, String field, Map<String, Object> map) throws NoSuchFieldException, IllegalAccessException {
        if (clazz == null) {
            return;
        }
        Field declaredField = clazz.getDeclaredField(field);
        declaredField.setAccessible(true);
        ExcelDictionary excelDictionary = declaredField.getAnnotation(ExcelDictionary.class);
        modifyAnnotationFieldValue(excelDictionary, map);
    }

    /**
     * 根据Class和Field修改{@link ExcelDictionary}注解的字段值,字段值来源为map
     * @param clazz clazz
     * @param field fieldName
     * @param dictionaryMap  字典 Map<String,String> dictionaryMap
     * @throws NoSuchFieldException throws by {@link Field#get}
     * @throws IllegalAccessException throws by {@link Field#set}
     */
    @SuppressWarnings("all")
    public static void modifyAnnotationExcelDictionary(Class<?> clazz, String field,Map<String,String> dictionaryMap) throws NoSuchFieldException, IllegalAccessException {
        if (CollectionUtils.isEmpty(dictionaryMap)) {
            return;
        }
        int size = dictionaryMap.size();
        final String[] keyArray = new String[size];
        final String[] valueArray = new String[size];
        final int[] index = {0};
        dictionaryMap.forEach((key,value)->{
            keyArray[index[0]] = key;
            valueArray[index[0]++] = value;
        });
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("keyArray",keyArray);
        map.put("valueArray",valueArray);
        modifyAnnotationExcelDictionaryMemberValues(clazz, field, map);
    }

    /**
     * 根据Class和Field修改{@link ExcelDictionary}注解的字段值,字段值来源为枚举类
     * @param clazz clazz
     * @param field fieldName
     * @param enumClazz 字典枚举类
     * @param keyName 枚举类中作为字典key 的字段名字
     * @param valueName 枚举类中作为字典value 的字段名字
     * @throws NoSuchFieldException throws by {@link Field#get}
     * @throws IllegalAccessException throws by {@link Field#set}
     * @throws InvocationTargetException throws by {@link Method#invoke(Object, Object...)}
     * @throws NoSuchMethodException throws by {@link Class#getDeclaredMethod(String, Class[])}
     */
    @SuppressWarnings("rawtypes")
    public static <T extends Enum> void modifyAnnotationExcelDictionary(Class<?> clazz, String field, Class<T> enumClazz, String keyName, String valueName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Map<String, String> enumToMap = enumToMap(enumClazz, keyName, valueName);
        modifyAnnotationExcelDictionary(clazz, field, enumToMap);
    }

    /**
     * 根据Class和Field修改{@link ExcelDictionary}注解的字段值,字段值来源为枚举类
     * @param clazz clazz
     * @param collection 传入的字典对象
     * @param field 要修改的字段
     * @param <N> N extends IDictionary
     * @throws NoSuchFieldException throws by {@link Field#get}
     * @throws IllegalAccessException throws by {@link Field#set}
     */
    public static <N extends IDictionary,M extends Collection<N>> void modifyAnnotationExcelDictionary(Class<?> clazz,M collection,String field) throws NoSuchFieldException, IllegalAccessException {
        Map<String, String> iDictionaryMap = iDictionaryToMap(collection);
        modifyAnnotationExcelDictionary(clazz, field, iDictionaryMap);
    }

    private static <N extends IDictionary,M extends Collection<N>> Map<String, String> iDictionaryToMap(M collection) {
        HashMap<String, String> map = new HashMap<>();
        collection.forEach(n -> map.put(n.getKey(), n.getValue()));
        return map;
    }

    /**
     * 字典枚举转Map
     * @param clazz 枚举类
     * @param keyName 字典中key在枚举类中的字段名字
     * @param valueName 字典中value在枚举类中的字段名字
     * @return Map<String, String> dictionaryMap
     * @throws NoSuchMethodException throws by {@link Class#getDeclaredMethod(String, Class[])}
     * @throws IllegalAccessException throws by {@link Field#set}
     * @throws InvocationTargetException throws by {@link Method#invoke(Object, Object...)}
     */
    @SuppressWarnings("all")
    public static <T extends Enum> Map<String, String> enumToMap(Class<T> clazz, String keyName, String valueName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        T[] enumConstants = clazz.getEnumConstants();
        Method getKey = clazz.getDeclaredMethod(toGetMethodName(keyName));
        Method getValue = clazz.getDeclaredMethod(toGetMethodName(valueName));
        Map<String, String> enumMap = new HashMap<>(enumConstants.length);
        for (T enumConstant : enumConstants) {
            enumMap.put(String.valueOf(getKey.invoke(enumConstant)),String.valueOf(getValue.invoke(enumConstant)));
        }
        return enumMap;
    }

    /**
     * 根据fieldName获取Getter方法
     * @param fieldName
     * @return
     */
    @SuppressWarnings("all")
    public static String toGetMethodName(String fieldName) {
        char c = Character.toUpperCase(fieldName.charAt(0));
        return new StringBuilder("get").append(c).append(fieldName.substring(1)).toString();
    }

}
