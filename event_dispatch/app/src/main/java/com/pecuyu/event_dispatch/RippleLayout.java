package com.pecuyu.event_dispatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * <br/>Author: pecuyu
 * <br/>Date: 2018/5/21
 * <br/>TODO:
 */

public class RippleLayout extends LinearLayout {
    public int[] mLocationInScreen = new int[2];
    private int[] mTouchLocationInScreen = new int[2];
    private View mTouchTarget;
    private boolean isPressed;

    private Paint mPaint;
    private long INVALIDATE_DURATION = 16;

    private int mRippleRadius;
    private int mRippleRadiusGap;
    private int mMaxRippleRadius;
    private float mCenterX;
    private float mCenterY;
    private int mTargetLeft;
    private int mTargetTop;
    private int mTouchTargetWidth;
    private int mTargetRight;
    private int mTargetBottom;
    private int mMinTargetEdge;
    private int mMaxTargetEdge;
    private boolean canDrawRipple;

    private PerformClickAction performClickAction = new PerformClickAction();

    public RippleLayout(Context context) {
        super(context);
        init();
    }

    public RippleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.ripple_color));
    }


    class PerformClickAction implements Runnable {

        MotionEvent event;

        @Override
        public void run() {

            if (mTouchTarget == null || event == null || !mTouchTarget.isEnabled() ||
                    !isTouchPointInView(mTouchTarget, (int) event.getRawX(), (int) event.getRawY())) {
                return;
            }

            mTouchTarget.performClick();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mTouchTarget == null || !canDrawRipple) {
            return;
        }

        // 绘制圆形
        canvas.save();
        canvas.clipRect(mTargetLeft, mTargetTop, mTargetRight, mTargetBottom);
        canvas.drawCircle(mCenterX, mCenterY, mRippleRadius, mPaint);
        canvas.restore();

        // 检查边界及处理
        if (mRippleRadius <= mMaxRippleRadius) {
            postInvalidateDelayed(INVALIDATE_DURATION, mTargetLeft, mTargetTop, mTargetRight, mTargetBottom);
        } else if (!isPressed) {
            canDrawRipple = false;
            postInvalidateDelayed(INVALIDATE_DURATION, mTargetLeft, mTargetTop, mTargetRight, mTargetBottom);
            postDelayed(performClickAction, INVALIDATE_DURATION);
        }

        // 增加绘制半径
        if (mRippleRadius < mMinTargetEdge) {
            mRippleRadius += mRippleRadiusGap;
        } else {
            mRippleRadius += mRippleRadiusGap * 3;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int rawX = (int) ev.getRawX();
        int rawY = (int) ev.getRawY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchTarget = getTouchTargetViaLocation(this, rawX, rawY);
                if (mTouchTarget != null && mTouchTarget.isEnabled() && mTouchTarget.isClickable() && !canDrawRipple) {
                    initBeforeInvalidate(ev);
                    isPressed = true;
                    canDrawRipple = true;
                    postInvalidateDelayed(INVALIDATE_DURATION);
                }
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                postInvalidateDelayed(INVALIDATE_DURATION);
                performClickAction.event = ev;
                dispatchUpTouchEventToViewGroup(ev, rawX, rawY);
                return true;

            case MotionEvent.ACTION_CANCEL:
                isPressed = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 传递up事件给包含触摸点的ViewGroup
     *
     * @param event MotionEvent
     * @param x     x
     * @param y     y
     */
    private void dispatchUpTouchEventToViewGroup(MotionEvent event, int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != null && child instanceof ViewGroup) {
                if (isTouchPointInView(child, x, y)) {
                    child.dispatchTouchEvent(event);
                }
            }
        }
    }

    /**
     * 在绘制ripple之前，进行初始化
     *
     * @param ev MotionEvent
     */
    private void initBeforeInvalidate(MotionEvent ev) {
        mMaxTargetEdge = Math.max(mTouchTarget.getWidth(), mTouchTarget.getHeight());
        mMinTargetEdge = Math.min(mTouchTarget.getWidth(), mTouchTarget.getHeight());

        // center
        mCenterX = ev.getX();
        mCenterY = ev.getY();

        // bound
        mTouchTarget.getLocationOnScreen(mTouchLocationInScreen);
        mTargetLeft = mTouchLocationInScreen[0] - mLocationInScreen[0];
        mTargetTop = mTouchLocationInScreen[1] - mLocationInScreen[1];
        mTargetRight = mTargetLeft + mTouchTarget.getWidth();
        mTargetBottom = mTargetTop + mTouchTarget.getHeight();

        // radius
        mRippleRadius = 1;
        mRippleRadiusGap = mMinTargetEdge / 10;
        mTouchTargetWidth = mTouchTarget.getWidth();
        int transformedCenterX = (int) (mCenterX - mTargetLeft);
        mMaxRippleRadius = Math.max(mTouchTargetWidth - transformedCenterX, transformedCenterX);

    }

    /**
     * 通过位置坐标获取当前TouchTarget
     *
     * @param rawX 当前屏幕 X 位置
     * @param rawY 当前屏幕 Y 位置
     */
    private View getTouchTargetViaLocation(View root, int rawX, int rawY) {
        ArrayList<View> touchables = getTouchables();
        for (View child : touchables) {
            if (isTouchPointInView(child, rawX, rawY)) {
                return child;
            }
        }

        return null;
    }

    /**
     * 判断当前触摸的点是否在给定的view中
     *
     * @param target target
     * @param rawX   rawX
     * @param rawY   rawY
     * @return
     */
    private boolean isTouchPointInView(View target, int rawX, int rawY) {
        if (target == null) return false;

        target.getLocationOnScreen(mTouchLocationInScreen);
        int left = mTouchLocationInScreen[0];
        int top = mTouchLocationInScreen[1];
        int right = left + target.getWidth();
        int bottom = top + target.getHeight();
        return rawX >= left && rawX <= right && rawY >= top && rawY <= bottom;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.getLocationOnScreen(mLocationInScreen);
    }

}
