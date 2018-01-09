package com.stx.xhb.customwaterview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stx.xhb.customwaterview.model.WaterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xiaohaibin.
 * @time: 2018/1/5
 * @mail:xhb_199409@163.com
 * @github:https://github.com/xiaohaibin
 * @describe: 支付宝蚂蚁森林水滴能量
 */

public class WaterFlake extends FrameLayout {

    private List<WaterModel> modelList;
    private OnWaterItemListener mOnWaterItemListener;
    private List<Point> mPoints;
    /**
     * 中间小树View
     */
    private View mTreeView;

    public WaterFlake(@NonNull Context context) {
        super(context);
        init();
    }

    public WaterFlake(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterFlake(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPoints = new ArrayList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Rect rect = new Rect();
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).getHitRect(rect);
                if (rect.contains(x, y)) {
                    if (mOnWaterItemListener != null) {
                        getChildAt(i).performClick();
                        mOnWaterItemListener.onItemClick(i);
                        startAnimator(getChildAt(i));
                        return true;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    /**
     * 设置小球数据，根据数据集合创建小球数量
     *
     * @param modelList 数据集合
     * @param view      中间小树View
     */
    public void setModelList(List<WaterModel> modelList, View view) {
        this.modelList = modelList;
        mPoints.add(new Point(50, 200));
        mPoints.add(new Point(150, 300));
        mPoints.add(new Point(300, 200));
        mPoints.add(new Point(300, 350));
        for (int i = 0; i < mPoints.size(); i++) {
            Point point = mPoints.get(i);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(point.x, point.y, 0, 0);
            WaterView waterView = new WaterView(getContext());
            waterView.setLayoutParams(params);
            addView(waterView);
        }
    }

    /**
     * 设置小球点击事件
     *
     * @param onWaterItemListener
     */
    public void setOnWaterItemListener(OnWaterItemListener onWaterItemListener) {
        mOnWaterItemListener = onWaterItemListener;
    }

    public interface OnWaterItemListener {
        void onItemClick(int pos);
    }

    private void startAnimator(final View view) {

        ObjectAnimator translatAnimator = ObjectAnimator.ofFloat(view, "translationY", 0f, 300f);
        translatAnimator.start();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        alphaAnimator.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translatAnimator).with(alphaAnimator);
        animatorSet.setDuration(3000);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(view);
                requestLayout();
            }
        });
    }
}
