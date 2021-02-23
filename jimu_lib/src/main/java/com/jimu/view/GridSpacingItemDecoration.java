package com.jimu.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ljh on 2020/3/31.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int column;
    private int space;

    public GridSpacingItemDecoration(int column, int space) {
        this.column = column;
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        outRect.left = 0;
        outRect.right = space;
    }

}
