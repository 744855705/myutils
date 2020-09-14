package com.yanhongbin.workutil.localcache;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/24 2:30 下午
 */
public class CacheKeyUtil {

    private static final String WE_CHAT_CLIENT_ACCESS_TOKEN_CACHE_KEY = "WE_CHAT_CLIENT_ACCESS_TOKEN_CACHE_KEY";
    private static final String WE_CHAT_ACCESS_TOKEN_CACHE_KEY = "WE_CHAT_ACCESS_TOKEN_CACHE_KEY";
    private static final String WE_CHAT_JS_API_TICKET_CACHE_KEY = "WE_CHAT_JS_API_TICKET_CACHE_KEY";


    public static String makeKey(String prefix, Object... args) {
        StringBuilder builder = new StringBuilder(prefix);
        for (Object arg : args) {
            if (arg != null) {
                builder.append('_').append(arg);
            }else {
                builder.append('_').append("NULL");
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
