package com.yanhongbin.workutil.excel.enumerate;

/**
 * Created with IDEA
 * description: excel 单元格类型 ,不使用poi提供的类型
 * @author :YanHongBin
 * @date :Created in 2019/12/4 13:38
 */
public enum CellType {

    /**
     * 数字
     */
    NUMERIC(0),
    /**
     * 文本
     */
    STRING(1),
    /**
     * 公式
     */
    FORMULA(2),
    /**
     * 空白
     */
    BLANK(3),
    /**
     * 布尔
     */
    BOOLEAN(4),
    /**
     * 错误的类型
     */
    ERROR(5);

    Integer type;

    CellType(Integer type){
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
