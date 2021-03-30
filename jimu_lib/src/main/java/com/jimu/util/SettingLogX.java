package com.jimu.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Copyright (C), 2014-2020, 佛山云米科技有限公司
 *
 * @ProjectName: CommonSettingPage
 * @Package: com.viomi.settingpagelib.utils
 * @ClassName: SettingLogX
 * @Description: 日志工具
 * @Author: randysu
 * @CreateDate: 2020-02-24 15:01
 * @UpdateUser:
 * @UpdateDate: 2020-02-24 15:01
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SettingLogX {

    private static final String LOG_FORMATTER = "❖ %s/%s  ❖  %s";

    enum LogType {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        ASSERT
    }

    public static void v(String tag, String msg) {
        showLongLog(tag, null, msg, LogType.VERBOSE);
    }

    public static void v(String tag, String subTag, String msg) {
        showLongLog(tag, subTag, msg, LogType.VERBOSE);
    }

    public static void d(String tag, String msg) {
        showLongLog(tag, null, msg, LogType.DEBUG);
    }

    public static void d(String tag, String subTag, String msg) {
        showLongLog(tag, subTag, msg, LogType.DEBUG);
    }

    public static void i(String tag, String msg) {
        showLongLog(tag, null, msg, LogType.INFO);
    }

    public static void i(String tag, String subTag, String msg) {
        showLongLog(tag, subTag, msg, LogType.INFO);
    }

    public static void w(String tag, String msg) {
        showLongLog(tag, null, msg, LogType.WARN);
    }

    public static void w(String tag, String subTag, String msg) {
        showLongLog(tag, subTag, msg, LogType.WARN);
    }

    public static void e(String tag, String msg) {
        showLongLog(tag, null, msg, LogType.ERROR);
    }

    public static void e(String tag, String subTag, String msg) {
        showLongLog(tag, subTag, msg, LogType.ERROR);
    }

    public static void a(String tag, String msg) {
        showLongLog(tag, null, msg, LogType.ASSERT);
    }

    public static void a(String tag, String subTag, String msg) {
        showLongLog(tag, subTag, msg, LogType.ASSERT);
    }

    private static void showLongLog(String tag, String subTag, String msg, LogType type) {
        msg = msg.trim();
        int index = 0;
        int maxLength = 4000;

        String sub;
        while (index < msg.length()) {
            if (msg.length() <= index + maxLength) {
                sub = msg.substring(index);
            } else {
                sub = msg.substring(index, index + maxLength);
            }

            index += maxLength;

            if (type == LogType.VERBOSE) {
                if (TextUtils.isEmpty(subTag)) {
                    Log.v(tag, sub.trim());
                } else {
                    Log.v(tag, String.format(LOG_FORMATTER, ProcessUtils.getCurrentProcessName(), subTag, sub.trim()));
                }
            } else if (type == LogType.DEBUG) {
                if (TextUtils.isEmpty(subTag)) {
                    Log.d(tag, sub.trim());
                } else {
                    Log.d(tag, String.format(LOG_FORMATTER, ProcessUtils.getCurrentProcessName(), subTag, sub.trim()));
                }
            } else if (type == LogType.INFO) {
                if (TextUtils.isEmpty(subTag)) {
                    Log.i(tag, sub.trim());
                } else {
                    Log.i(tag, String.format(LOG_FORMATTER, ProcessUtils.getCurrentProcessName(), subTag, sub.trim()));
                }
            } else if (type == LogType.WARN) {
                if (TextUtils.isEmpty(subTag)) {
                    Log.w(tag, sub.trim());
                } else {
                    Log.w(tag, String.format(LOG_FORMATTER, ProcessUtils.getCurrentProcessName(), subTag, sub.trim()));
                }
            } else if (type == LogType.ERROR) {
                if (TextUtils.isEmpty(subTag)) {
                    Log.e(tag, sub.trim());
                } else {
                    Log.e(tag, String.format(LOG_FORMATTER, ProcessUtils.getCurrentProcessName(), subTag, sub.trim()));
                }
            } else if (type == LogType.ASSERT) {
                if (TextUtils.isEmpty(subTag)) {
                    Log.wtf(tag, sub.trim());
                } else {
                    Log.wtf(tag, String.format(LOG_FORMATTER, ProcessUtils.getCurrentProcessName(), subTag, sub.trim()));
                }
            } else {
                if (TextUtils.isEmpty(subTag)) {
                    Log.v(tag, sub.trim());
                } else {
                    Log.v(tag, String.format(LOG_FORMATTER, ProcessUtils.getCurrentProcessName(), subTag, sub.trim()));
                }
            }
        }
    }

}
