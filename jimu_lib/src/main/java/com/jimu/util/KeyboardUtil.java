package com.jimu.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Ljh on 2021/3/26.
 * Description:
 */
public class KeyboardUtil {
    /**
     * 隐藏和显示输入法
     */
    public static void showOrHideKeyboard(Context context, View view, boolean isShow) {
        if (context == null || view == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) return;
        if (isShow) {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

