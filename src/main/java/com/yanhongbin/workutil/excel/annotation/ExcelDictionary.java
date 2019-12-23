package com.yanhongbin.workutil.excel.annotation;

import java.lang.annotation.*;
import java.util.Map;

/**
 * Created with IDEA
 * description: 标识字段类型,是否为字典需要和{@link Excel}配合使用,
 *              如果需要动态的使用字典需要搭配
 *              {@link AnnotationFieldModify#modifyAnnotationExcelDictionary(Class, String, Map)})
 *              动态的修改字典
 *
 * @author :YanHongBin
 * @date :Created in2019/12/19 9:19
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExcelDictionary {

    /**
     * keyArray与valueArray应按顺序一一对应
     * @return
     */
    String[] keyArray() default {};

    /**
     * valueArray与keyArray应按顺序一一对应
     * @return
     */
    String[] valueArray() default {};

}
