package com.stx.xhb.customwaterview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.stx.xhb.customwaterview.model.WaterModel;

import java.util.List;

/**
 * @author: xiaohaibin.
 * @time: 2018/1/5
 * @mail:xhb_199409@163.com
 * @github:https://github.com/xiaohaibin
 * @describe: 支付宝蚂蚁森林水滴能量
 */

public class WaterFlake extends FrameLayout {

    private OnWaterItemListener mOnWaterItemListener;
    /**
     * 小树坐标X
     */
    private float treeCenterX = 0;
    /**
     * 小树坐标Y
     */
    private float treeCenterY = 0;
    /**
     * 小树高度
     */
    private int radius = 80;
    /**
     * 开始角度
     */
    private double mStartAngle = 0;
    /**
     * 是否正在收集能量
     */
    private boolean isCollect = false;

    private float panding = 50;

    private float mWidth, mHeight;
    private LayoutInflater mLayoutInflater;


    public WaterFlake(@NonNull Context context) {
        this(context, null);
    }

    public WaterFlake(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterFlake(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
                        collectAnimator(getChildAt(i));
                        return true;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void init() {
        mLayoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - panding;
        mHeight = h - panding;
        Log.i("===>w", w + "");
        Log.i("===>h", h + "");
    }

    /**
     * 设置小球数据，根据数据集合创建小球数量
     *
     * @param modelList 数据集合
     */
    public void setModelList(final List<WaterModel> modelList, float treeCenterX, float treeCenterY) {
        if (modelList == null || modelList.isEmpty()) {
            return;
        }
        this.treeCenterX = treeCenterX;
        this.treeCenterY = treeCenterY;
        removeAllViews();
        post(new Runnable() {
            @Override
            public void run() {
                addWaterView(modelList);
            }
        });
    }

    private void addWaterView(List<WaterModel> modelList) {
        for (int i = 0; i < modelList.size(); i++) {
            View view = mLayoutInflater.inflate(R.layout.item_water, this,false);
            view.setX(mWidth * Utils.getRandom(0.1F, 0.8F));
            view.setY(mHeight * Utils.getRandom(0.16F, 0.7F));
            addView(view);
            addShowViewAnimation(view);
            start(view);
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

    private void collectAnimator(final View view) {
        if (isCollect) {
            return;
        }
        isCollect = true;

        ObjectAnimator translatAnimatorY = ObjectAnimator.ofFloat(view, "translationY", getTreeCenterY());
        translatAnimatorY.start();

        ObjectAnimator translatAnimatorX = ObjectAnimator.ofFloat(view, "translationX", getTreeCenterX());
        translatAnimatorX.start();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        alphaAnimator.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translatAnimatorY).with(translatAnimatorX).with(alphaAnimator);
        animatorSet.setDuration(3000);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(view);
                isCollect = false;
            }
        });
    }

    public void start(View view) {
        ObjectAnimator mAnimator = ObjectAnimator.ofFloat(view, "translationY", -6f+view.getY(),view.getY()+6f, -6.0f+view.getY());
        mAnimator.setDuration(3500);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }

    /**
     * 添加显示动画
     * @param view
     */
    private void addShowViewAnimation(View view) {
        view.setAlpha(0);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().alpha(1).scaleX(1).scaleY(1).setDuration(500).start();
    }


    public float getTreeCenterX() {
        return treeCenterX;
    }

    public float getTreeCenterY() {
        return treeCenterY;
    }
}
