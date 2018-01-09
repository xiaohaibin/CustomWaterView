package com.stx.xhb.customwaterview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
        modelList = new ArrayList<>();
        mPoints = new ArrayList<>();
        mPoints.add(new Point(300, 400));
        mPoints.add(new Point(400, 500));
        mPoints.add(new Point(500, 400));
        for (int i = 0; i < mPoints.size(); i++) {
            Point point = mPoints.get(i);
            WaterView waterView = new WaterView(getContext(), point.x, point.y);
            addView(waterView);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Log.i("===>x", x + "==");
            Log.i("===>y", y + "==");
            Rect rect = new Rect();
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).getHitRect(rect);
                if (rect.contains(x,  y)) {
                    if (mOnWaterItemListener != null) {
                        getChildAt(i).performClick();
                        mOnWaterItemListener.onItemClick();
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

    public void setModelList(List<WaterModel> modelList) {
        this.modelList = modelList;
    }

    public void setOnWaterItemListener(OnWaterItemListener onWaterItemListener) {
        mOnWaterItemListener = onWaterItemListener;
    }

    public interface OnWaterItemListener {
        void onItemClick();
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
                invalidate();
            }
        });
    }

    /**
     * 根据触摸位置坐标判断是否在View的区域内
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        return y >= top && y <= bottom && x >= left && x <= right;
    }
}
