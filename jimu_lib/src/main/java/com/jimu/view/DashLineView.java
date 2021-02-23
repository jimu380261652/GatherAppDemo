package com.jimu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DashLineView extends View {

    private Paint mPaint;

    public DashLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mPaint.setColor(getResources().getColor(R.color.color_white_alpha_40));
        mPaint.setStrokeWidth(1);
        mPaint.setPathEffect(new DashPathEffect(new float[] {5, 5}, 0));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerY = getHeight() / 2;
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawLine(0, centerY, getWidth(), centerY, mPaint);
    }
}
