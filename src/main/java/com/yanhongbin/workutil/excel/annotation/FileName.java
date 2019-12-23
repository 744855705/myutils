package com.yanhongbin.workutil.excel.annotation;

import java.lang.annotation.*;

/**
 * Created with IDEA
 * description:
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/20 17:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FileName {

    String value();
}
