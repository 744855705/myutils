package com.yanhongbin.workutil.excel.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IDEA
 * description: Fields������,������Field��ز���
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/11 16:07
 */
public class FieldUtil {

    /**
     * ��ȡClass��������ֶ�,���������ֶ�
     *
     * @param clazz Class����
     * @return Field[]
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        ArrayList<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * ��{@param clazz}�л�ȡ���� {@param annotationClazz} ע����ֶ�
     * @param clazz ��ȡfield ���ֶ�
     * @param annotationClazz ע����
     * @return List<Field>
     */
    public static List<Field> getFieldsListWithAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotationClazz) {
        final List<Field> allFields =getAllFields(clazz);
        final List<Field> annotatedFields = new ArrayList<>();
        for (final Field field : allFields) {
            if (field.getAnnotation(annotationClazz) != null) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }



}
