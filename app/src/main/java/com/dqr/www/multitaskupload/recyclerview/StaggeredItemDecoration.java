package com.dqr.www.multitaskupload.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description：瀑布流间距
 * Author：Vinci
 * Date： 2017/1/17 2017
 */
public class StaggeredItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public StaggeredItemDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left=space;
        outRect.right=space;
        outRect.bottom=space;
        //注释这两行是为了上下间距相同
//        if(parent.getChildAdapterPosition(view)==0){
        outRect.top=space;
//        }
    }
}
