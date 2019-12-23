package com.yanhongbin.workutil.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IDEA
 * description:
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/23 8:49
 */
public class RequestUtil {

    /**
     * 获取request, 线程安全
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest(){
        // 获取request, 线程安全
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }
}
