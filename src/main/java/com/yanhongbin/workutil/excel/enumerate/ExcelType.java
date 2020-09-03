package com.yanhongbin.workutil.excel.enumerate;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/3 11:29 上午
 */
public enum ExcelType {
    /**
     * 默认使用 xls
     */
    NONE(".xls"),

    /**
     * xls 类型文件
     */
    XLS(".xls"),

    /**
     * xlsx 类型文件
     */
    XLSX(".xlsx"),
    ;


    /**
     * 后缀
     */
    private final String suffix;

    ExcelType(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
