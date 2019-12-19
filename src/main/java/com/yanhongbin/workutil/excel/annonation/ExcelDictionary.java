package com.yanhongbin.workutil.excel.annonation;

import com.yanhongbin.workutil.excel.enumerate.CellType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Created with IDEA
 * description: 标识字段类型,是否为字典需要和{@link Excel}配合使用
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
