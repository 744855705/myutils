package com.yanhongbin.workutil.excel.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
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
    public static void modifyAnnotationFieldValue(Annotation annotation, Map map) throws NoSuchFieldException, IllegalAccessException {
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
     * @param clazz clazz
     * @param field fieldName
     * @param map memberValues
     * @throws NoSuchFieldException throws by {@link Field#get}
     * @throws IllegalAccessException throws by {@link Field#set}
     */
    private static void modifyAnnotationExcelDictionaryMemberValues(Class<?> clazz, String field,Map<String, Object> map) throws NoSuchFieldException, IllegalAccessException {
        if(clazz == null){
            return;
        }
        Field declaredField = clazz.getDeclaredField(field);
        declaredField.setAccessible(true);
        ExcelDictionary excelDictionary = declaredField.getAnnotation(ExcelDictionary.class);
        modifyAnnotationFieldValue(excelDictionary, map);
    }

    /**
     * 根据Class和Field修改{@link ExcelDictionary}注解的字段值
     * @param clazz clazz
     * @param field fieldName
     * @param dictionaryMap  字典 Map<String,String> dictionaryMap
     * @throws NoSuchFieldException throws by {@link Field#get}
     * @throws IllegalAccessException throws by {@link Field#set}
     */
    public static void modifyAnnotationExcelDictionary(Class<?> clazz, String field,Map<String,String> dictionaryMap) throws NoSuchFieldException, IllegalAccessException {
        if (dictionaryMap == null) {
            return;
        }
        int size = dictionaryMap.size();
        if (size == 0) {
            return;
        }
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
        modifyAnnotationExcelDictionaryMemberValues(clazz, field,map);
    }
}
