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
import android.view.View;

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
    private float strokeWidth = 1;
    /**
     * 文字字体大小
     */
    private float textSize = 36;
    /**
     * 球心X坐标
     */
    private float mCenterX;
    /**
     * 球心Y坐标
     */
    private float mCenterY;
    /**
     * 水滴球半径
     */
    private int mRadius = 30;
    /**
     * 圆球文字内容
     */
    private String textContent="3g";

    public WaterView(Context context) {
        super(context);
        init();
    }

    public WaterView(Context context, float centerX, float centerY) {
        super(context);
        init();
        mCenterX = centerX;
        mCenterY = centerY;
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
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        canvas.translate(width / 2, height / 2);

        drawCircleView(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec),measureSize(heightMeasureSpec));
    }

    private int measureSize(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = Utils.dp2px(getContext(),2 * mRadius + 2);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
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
        canvas.drawCircle(mCenterX, mCenterY, Utils.dp2px(getContext(), mRadius), paint);

        //描边
        paint.setColor(storkeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(mCenterX, mCenterY, Utils.dp2px(getContext(), mRadius) + strokeWidth, paint);

        //圆球文字
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        drawVerticalText(canvas, mCenterX, mCenterY, textContent);
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

    public void setCenterX(float centerX) {
        mCenterX = centerX;
    }

    public void setCenterY(float centerY) {
        mCenterY = centerY;
    }

    public float getCenterX() {
        return mCenterX;
    }

    public float getCenterY() {
        return mCenterY;
    }
}
