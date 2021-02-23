package com.yanhongbin.workutil.cache.localcache;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/24 2:30 下午
 */
public class CacheKeyUtil {

    /**
     * 拼接参数分隔符
     */
    private static final Character DELIMITER = '_';

    /**
     * 参数为null的时候拼接的值
     */
    private static final String NULL_VALUE = "NULL";

    private static final String WE_CHAT_CLIENT_ACCESS_TOKEN_CACHE_KEY = "WE_CHAT_CLIENT_ACCESS_TOKEN_CACHE_KEY";
    private static final String WE_CHAT_ACCESS_TOKEN_CACHE_KEY = "WE_CHAT_ACCESS_TOKEN_CACHE_KEY";
    private static final String WE_CHAT_JS_API_TICKET_CACHE_KEY = "WE_CHAT_JS_API_TICKET_CACHE_KEY";


    public static String makeKey(String prefix, Object... args) {
        StringBuilder builder = new StringBuilder(prefix);
        for (Object arg : args) {
            if (arg != null) {
                builder.append(DELIMITER).append(arg);
            }else {
                builder.append(DELIMITER).append(NULL_VALUE);
            }
        }
        return builder.toString().intern();
    }

    public static String makeWeChatClientAccessTokenCacheKey(Object... args){
        return makeKey(WE_CHAT_CLIENT_ACCESS_TOKEN_CACHE_KEY, args);
    }

    public static String makeWeChatAccessTokenCacheKey(Object... args){
        return makeKey(WE_CHAT_ACCESS_TOKEN_CACHE_KEY, args);
    }

    public static String makeWeChatJsApiTicketCacheKey(Object... args){
        return makeKey(WE_CHAT_JS_API_TICKET_CACHE_KEY, args);
    }

}
