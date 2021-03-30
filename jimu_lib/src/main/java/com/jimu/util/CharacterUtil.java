package com.jimu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ljh on 2021/3/25.
 * Description:
 */
public class CharacterUtil {
    private static final String TAG = "CharacterUtil";


    /**
     * 判断字符串是否包含中文
     */
    public static boolean isChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}

