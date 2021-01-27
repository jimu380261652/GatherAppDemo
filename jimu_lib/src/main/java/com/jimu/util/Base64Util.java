package com.jimu.util;

import android.util.Base64;

/**
 * Created by Ljh on 2020/12/9.
 * Description:
 */
public class Base64Util {
    public static String encode(String text){
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
        //return "";
    }

    public static String decode(String text){
        return new String(Base64.decode(text.getBytes(), Base64.DEFAULT));
    }
}

