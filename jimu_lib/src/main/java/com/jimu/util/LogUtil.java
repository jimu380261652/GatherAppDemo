package com.jimu.util;
import android.text.TextUtils;
import android.util.Log;

/**
 * 日志打印管理
 */
public class LogUtil {
    private static boolean logEnable = false;


    public static void initLog(boolean enable) {
        logEnable = enable;
        //if (BuildConfig.LOG_DEBUG)
        //    logEnable = true;
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty("") ? tag : "" + ":" + tag;
        return tag;
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void d(String TAG, String msg) {
        if (logEnable && !TextUtils.isEmpty(msg)) {
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            Log.d(tag, TAG + " " + msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (logEnable && !TextUtils.isEmpty(msg)) {
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            Log.e(tag, TAG + " " + msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (logEnable && !TextUtils.isEmpty(msg)) {
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            Log.i(tag, TAG + " " + msg);
        }
    }

    public static void w(String TAG, String msg) {
        if (logEnable && !TextUtils.isEmpty(msg)) {
            StackTraceElement caller = getCallerStackTraceElement();
            String tag = generateTag(caller);
            Log.w(tag, TAG + " " + msg);
        }
    }
}
