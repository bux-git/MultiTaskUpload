/*
 * Copyright (c) 2015.
 * 湖南球谱科技有限公司版权所有
 * Hunan Qiupu Technology Co., Ltd. all rights reserved.
 */

package com.dqr.www.multitaskupload.recyclerview;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class RecyclerHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;

    public RecyclerHolder(View itemView) {
        super(itemView);
        //一般不会超过8个吧
        this.mViews = new SparseArray<View>(12);
    }
    public RecyclerHolder(View itemView, int count) {
        super(itemView);
        //一般不会超过8个吧
        this.mViews = new SparseArray<View>(count);
    }

    public SparseArray<View> getAllView() {
        return mViews;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerHolder setText(int viewId, int text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }
    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param colorResId
     * @return
     */
    public RecyclerHolder setTextColor(int viewId, int colorResId) {
        TextView view = getView(viewId);
        view.setTextColor(itemView.getResources().getColor(colorResId));
        return this;
    }

    /**
     * 为TextView设置字体
     *
     * @param viewId
     * @param sizeResId
     * @return
     */
    public RecyclerHolder setTextSize(int viewId, int sizeResId) {
        TextView view = getView(viewId);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX
                ,itemView.getContext().getResources().getDimensionPixelOffset(sizeResId));
        return this;
    }

    /**
     * 为TextView设置粗体等
     *
     * @param viewId
     * @param
     * @return
     */
    public RecyclerHolder setTextStyle(int viewId, boolean isBold) {
        TextView view = getView(viewId);
        view.getPaint().setFakeBoldText(isBold);
        return this;
    }
    /**
     * 为View设置background
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public RecyclerHolder setBackgroundResource(int viewId, int drawableId) {
        View view = getView(viewId);
        view.setBackgroundResource(drawableId);
        return this;
    }


    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public RecyclerHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }




    /**
     * 为LinearLayout设置background
     *
     * @param viewId
     * @param bac
     * @return
     */
    public RecyclerHolder setLinearLayoutBackgroundResource(int viewId, int bac) {
        LinearLayout view = getView(viewId);
        view.setBackgroundResource(bac);
        return this;
    }
    /**
     * 为RelativeLayout设置background
     *
     * @param viewId
     * @param bac
     * @return
     */
    public RecyclerHolder setRelativeLayoutBackgroundResource(int viewId, int bac) {
        RelativeLayout view = getView(viewId);
        view.setBackgroundResource(bac);
        return this;
    }

    /**
     * 为ImageView设置图片,设置加载时图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public RecyclerHolder setImageResource(int viewId, String url, int defaultResId) {
        ImageView view = getView(viewId);
        Glide.with(view.getContext()).load(url).dontAnimate().placeholder(defaultResId).into(view);
        return this;
    }


    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public RecyclerHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }


}