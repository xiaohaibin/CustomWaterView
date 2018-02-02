package com.stx.xhb.customwaterview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.stx.xhb.customwaterview.model.WaterModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author: xiaohaibin.
 * @time: 2018/1/5
 * @mail:xhb_199409@163.com
 * @github:https://github.com/xiaohaibin
 * @describe: 支付宝蚂蚁森林水滴能量
 */

public class WaterFlake extends FrameLayout {
    private static final int WHAT_ADD_PROGRESS = 1;
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
     * 是否正在收集能量
     */
    private boolean isCollect = false;
    /**
     * view变化的y抖动范围
     */
    private static final int CHANGE_RANGE = 10;
    /**
     * 控制抖动动画执行的快慢
     */
    public static final int PROGRESS_DELAY_MILLIS = 12;
    /**
     * 控制水滴动画的偏移量
     */
    private List<Float> mOffsets = Arrays.asList(5.0f, 4.5f, 4.8f, 5.5f, 5.8f, 6.0f, 6.5f);
    private Random mRandom = new Random();
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

    private void init() {
        mLayoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
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

    /**
     * 设置小球数据，根据数据集合创建小球数量
     *
     * @param modelList 数据集合
     */
    public void setModelList(final List<WaterModel> modelList, View view) {
        if (modelList == null || modelList.isEmpty()) {
            return;
        }
        this.treeCenterX = view.getX();
        this.treeCenterY = view.getY();
        removeAllViews();
        post(new Runnable() {
            @Override
            public void run() {
                addWaterView(modelList);
            }
        });

    }

    private void addWaterView(List<WaterModel> modelList) {
        int[] xRandom = randomCommon(1, 8, modelList.size());
        int[] yRandom = randomCommon(1, 7, modelList.size());
        if (xRandom == null || yRandom == null) {
            return;
        }
        for (int i = 0; i < modelList.size(); i++) {
            WaterModel waterModel = modelList.get(i);
            final View view = mLayoutInflater.inflate(R.layout.item_water, this, false);
            view.setX((float) ((mWidth * xRandom[i] * 0.11)));
            view.setY((float) ((mHeight * yRandom[i] * 0.08)));
            view.setTag(waterModel);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = v.getTag();
                    if (tag instanceof WaterModel) {
                        if (mOnWaterItemListener != null) {
                            mOnWaterItemListener.onItemClick((WaterModel) tag);
                            collectAnimator(view);
                        }
                    }
                }
            });
            view.setTag(R.string.isUp, mRandom.nextBoolean());
            setOffset(view);
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
        void onItemClick(WaterModel waterModel);
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
        boolean isUp = (boolean) view.getTag(R.string.isUp);
        float offset = (float) view.getTag(R.string.offset);
        ObjectAnimator mAnimator = null;
        if (isUp) {
            mAnimator = ObjectAnimator.ofFloat(view, "translationY", view.getY() - offset, view.getY() + offset, view.getY() - offset);
        } else {
            mAnimator = ObjectAnimator.ofFloat(view, "translationY", view.getY() + offset, view.getY() - offset, view.getY() + offset);
        }
        mAnimator.setDuration(1800);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }


    /**
     * 添加显示动画
     *
     * @param view
     */
    private void addShowViewAnimation(View view) {
        view.setAlpha(0);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().alpha(1).scaleX(1).scaleY(1).setDuration(500).start();
    }

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     */
    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) ((Math.random() * (max - min)) + min);
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }


    public float getTreeCenterX() {
        return treeCenterX;
    }

    public float getTreeCenterY() {
        return treeCenterY;
    }

    /**
     * 设置View的offset
     *
     * @param view
     */
    private void setOffset(View view) {
        float offset = mOffsets.get(mRandom.nextInt(mOffsets.size()));
        view.setTag(R.string.offset, offset);
    }

}
