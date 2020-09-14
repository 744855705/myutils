package com.yanhongbin.workutil.excel.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IDEA
 * description: Fields工具类,反射中Field相关操作
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/11 16:07
 */
public class FieldUtil {

    /**
     * 获取Class里的所有字段,包括父类字段
     *
     * @param clazz Class对象
     * @return List<Field>
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        ArrayList<Field> allFields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * 从{@param clazz}中获取带有 {@param annotationClazz} 注解的字段
     * @param clazz 获取field 的类
     * @param annotationClazz 注解类
     * @return List<Field>
     */
    public static List<Field> getFieldsListWithAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotationClazz) {
        final List<Field> allFields =getAllFields(clazz);
        final List<Field> annotatedFields = new LinkedList<>();
        for (final Field field : allFields) {
            if (field.getAnnotation(annotationClazz) != null) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }

}
