package com.yanhongbin.workutil.excel.exception;

/**
 * Created with IDEA
 * description: 注解没有找到
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/18 16:14
 */
public class AnnotationNotFoundException extends Exception {

    public AnnotationNotFoundException(Class<?> clazz,Class<?> annotationClazz) {
        super(new StringBuilder().append("在 ").append(clazz.getName()).append(" 中不能找到注解  ").append(annotationClazz.getName()).toString());
    }
}
