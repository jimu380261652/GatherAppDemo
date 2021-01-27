package com.jimu.util;

import java.text.DecimalFormat;

/**
 * Created by Ljh on 2021/1/19.
 * Description:
 */
public class DigitalUtil {
    static DecimalFormat mDecimalFormat = new DecimalFormat("00");

    public static String format00(int i){
        return mDecimalFormat.format(i);
    }
}

