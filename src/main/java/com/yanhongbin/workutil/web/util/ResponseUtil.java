package com.yanhongbin.workutil.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IDEA
 * description:
 *
 * @author :YanHongBin
 * @date :Created in 2019/12/23 8:50
 */
public class ResponseUtil {
    /**
     * 获取response 线程安全
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse(){
        // 获取response 线程安全
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
    }
}
