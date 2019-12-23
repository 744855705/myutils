package com.yanhongbin.workutil.excel.exception;

/**
 * Created with IDEA
 * description: Excel导出字段被标识为字典类型,但是在进行字典匹配的时候失败,抛出该异常(keyArray和valueArray长度不一致时也会抛出此异常)
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/19 10:48
 */
public class ExcelDictionaryMatchException extends Exception{

    public ExcelDictionaryMatchException(String message){
        super(message);
    }
}
