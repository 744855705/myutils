package com.yanhongbin.workutil.excel.annotation;

import com.yanhongbin.workutil.excel.enumerate.CellType;

import java.lang.annotation.*;

/**
 * Created with IDEA
 * description: excel 导入导出 标识字段 (导出需要该类有无参构造函数)
 * @author :YanHongBin
 * @date :Created in2019/12/4 13:21
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Excel {

    /**
     * 导出导入时对应的表头
     */
    String value();

    /**
     * 类型 默认String
     */
    CellType type() default CellType.STRING;
}
