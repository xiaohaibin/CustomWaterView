package com.stx.xhb.customwaterview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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

    public WaterFlake(@NonNull Context context) {
        super(context);
    }

    public WaterFlake(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterFlake(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        int left, top;
        // 根据tem的个数，计算角度
        float angleDelay = -180 / childCount;
        for (int i = 0; i < childCount; i++) {
            WaterView child = (WaterView) getChildAt(i);
            mStartAngle %= 180;

            //设置CircleView小圆点的坐标信息
            //坐标 = 旋转角度 * 半径 * 根据远近距离的不同计算得到的应该占的半径比例
//            则圆上任一点为：（x1,y1）
//            x1   =   x0   +   r   *   cos(ao   *   π   /180   )
//            y1   =   y0   +   r   *   sin(ao   *   π   /180   )
            if (child.getVisibility() != GONE) {
                left = (int) (getTreeCenterX() + radius * Math.cos(mStartAngle * Math.PI / 180) * (child.getProportion() / radius * 2));
                top = (int) (getTreeCenterY() + radius * Math.sin(mStartAngle * Math.PI / 180) * (child.getProportion() / radius * 2));
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredWidth());
            }
            mStartAngle += angleDelay;
        }
    }

    /**
     * 设置小球数据，根据数据集合创建小球数量
     *
     * @param modelList 数据集合
     */
    public void setModelList(List<WaterModel> modelList, float treeCenterX, float treeCenterY) {
        this.treeCenterX = treeCenterX;
        this.treeCenterY = treeCenterY;
        removeAllViews();
        for (int i = 0; i < modelList.size(); i++) {
            WaterView waterView = new WaterView(getContext(), (i + 1) + "g");
            waterView.setProportion(Utils.getRandom(radius, radius + 80));
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
        if (isCollect) {
            return;
        }
        isCollect = true;

        ObjectAnimator translatAnimatorY = ObjectAnimator.ofFloat(view, "translationY", getTreeCenterY());
        translatAnimatorY.start();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        alphaAnimator.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translatAnimatorY).with(alphaAnimator);
        animatorSet.setDuration(3000);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeViewInLayout(view);
                isCollect = false;
            }
        });
    }

    public float getTreeCenterX() {
        return treeCenterX;
    }

    public float getTreeCenterY() {
        return treeCenterY;
    }
}
