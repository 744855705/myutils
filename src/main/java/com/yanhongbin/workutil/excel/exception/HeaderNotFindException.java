package com.yanhongbin.workutil.excel.exception;

/**
 * Created with IDEA
 * description: 传入的表头参数不能与实体类标志的参数一一对应时抛出的异常
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/11 15:57
 */
public class HeaderNotFindException extends Exception {

    public HeaderNotFindException(String message) {
        super(message);
    }
}
