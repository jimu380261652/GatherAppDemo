package com.jimu.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created with Android Studio
 * Author:Ljh
 * Date:2020/1/21
 **/
public class ToastUtil {
    static Toast toast;
    public static void show(Context context,String msg,int duration){
        if(toast != null){
            toast.cancel();
            toast = null;
        }
        TextView tvView = new TextView(context.getApplicationContext());
        tvView.setText(msg);
        tvView.setGravity(Gravity.CENTER);
        tvView.setTextSize(TypedValue.COMPLEX_UNIT_PX,40);
        tvView.setTextColor(Color.parseColor("#ffffff"));
        tvView.setPadding(60,32,60,32);
        GradientDrawable gradientDrawable  = new GradientDrawable();
        //gradientDrawable .setGradientType(GradientDrawable.RECTANGLE);
        gradientDrawable .setCornerRadius(16);
        //gradientDrawable .setStroke(1, Color.parseColor("#25ffffff"));
        gradientDrawable .setColor(Color.parseColor("#CCFFFFFF"));
        tvView.setBackgroundDrawable(gradientDrawable);
        //
        Toast toast = new Toast(context.getApplicationContext());
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(tvView);
        toast.show();
    }

    public static void dismiss(){
        if(toast != null){
            toast.cancel();
            toast = null;
        }
    }
}
