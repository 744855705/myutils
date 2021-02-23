package com.yanhongbin.workutil.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description: log util slf4j
 *
 * @author yhb
 * @version 1.0
 * @date 2021/2/23 11:16
 */
public class LogUtil {

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void info(Logger log, String msg) {
        if (log.isInfoEnabled()) {
            log.info(msg);
        }
    }

    public static void info(Logger log, String format, Object... arguments) {
        if (log.isInfoEnabled()) {
            log.info(format, arguments);
        }
    }

    public static void debug(Logger log, String msg) {
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
    }

    public static void debug(Logger log, String format, Object... arguments) {
        if (log.isDebugEnabled()) {
            log.debug(format, arguments);
        }
    }

    public static void warn(Logger log, String msg){
        if (log.isWarnEnabled()) {
            log.warn(msg);
        }
    }

    public static void warn(Logger log, String format, Object... arguments) {
        if (log.isWarnEnabled()) {
            log.warn(format, arguments);
        }
    }

    public static void error(Logger log, String msg) {
        if (log.isErrorEnabled()) {
            log.error(msg);
        }
    }

    public static void error(Logger log, String msg, Throwable t) {
        if (log.isErrorEnabled()) {
            log.error(msg, t);
        }
    }

    public static void error(Logger log, String format, Object... arguments) {
        if (log.isErrorEnabled()) {
            log.error(format, arguments);
        }
    }

    public static void trace(Logger log, String msg) {
        if (log.isTraceEnabled()) {
            log.trace(msg);
        }
    }

    public static void trace(Logger log, String msg, Throwable t) {
        if (log.isTraceEnabled()) {
            log.trace(msg, t);
        }
    }

    public static void trace(Logger log, String format,Object... arguments) {
        if (log.isTraceEnabled()) {
            log.trace(format, arguments);
        }
    }

}
