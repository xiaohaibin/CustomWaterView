package com.stx.xhb.customwaterview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author: xiaohaibin.
 * @time: 2018/1/5
 * @mail:xhb_199409@163.com
 * @github:https://github.com/xiaohaibin
 * @describe: 自定义仿支付宝蚂蚁森林水滴View
 */

public class WaterView extends View {

    private Paint paint;
    private ObjectAnimator mAnimator;
    /**
     * 文字颜色
     */
    private int textColor = Color.parseColor("#69c78e");
    /**
     * 水滴填充颜色
     */
    private int waterColor = Color.parseColor("#c3f593");
    /**
     * 球描边颜色
     */
    private int storkeColor = Color.parseColor("#69c78e");
    /**
     * 描边线条宽度
     */
    private float strokeWidth = 0.5f;
    /**
     * 文字字体大小
     */
    private float textSize = 36;
    /**
     * 根据远近距离的不同计算得到的应该占的半径比例
     */
    private float proportion;
    /**
     * 水滴球半径
     */
    private int mRadius = 30;
    /**
     * 圆球文字内容
     */
    private String textContent="";

    public WaterView(Context context,String textContent) {
        super(context);
        this.textContent=textContent;
        init();
    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawCircleView(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(Utils.dp2px(getContext(), (int) (2 * (mRadius+strokeWidth))),Utils.dp2px(getContext(), (int) (2 * (mRadius+strokeWidth))));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("====》X",getX()+"==");
        Log.i("====》Y",getY()+"==");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    private void drawCircleView(Canvas canvas){
        //圆球
        paint.setColor(waterColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(Utils.dp2px(getContext(), mRadius), Utils.dp2px(getContext(), mRadius), Utils.dp2px(getContext(), mRadius), paint);

        //描边
        paint.setColor(storkeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Utils.dp2px(getContext(), (int) strokeWidth));
        canvas.drawCircle(Utils.dp2px(getContext(), mRadius), Utils.dp2px(getContext(), mRadius), Utils.dp2px(getContext(), (int) (mRadius+strokeWidth)) , paint);

        //圆球文字
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        drawVerticalText(canvas, Utils.dp2px(getContext(), mRadius), Utils.dp2px(getContext(), mRadius), textContent);
    }

    private void drawVerticalText(Canvas canvas, float centerX, float centerY, String text) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2;
        float textWidth = paint.measureText(text);
        float startX = centerX - textWidth / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, paint);
    }


    public void start() {
        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(this, "translationY", -6.0f, 6.0f, -6.0f);
            mAnimator.setDuration(3500);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.start();
        } else if (!mAnimator.isStarted()) {
            mAnimator.start();
        }
    }

    public void stop() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }
}
