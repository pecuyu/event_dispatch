package com.pecuyu.event_dispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;


/**
 * <br/>Author: pecuyu
 * <br/>Date: 2018/5/17
 * <br/>TODO:
 */

public class MyListView extends ListView {
    private float mStartY = 0;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        boolean b = super.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("MyListView", "ACTION_DOWN");
                requestDisallowInterceptTouchEvent(true); // 请求处理事件
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("MyListView", "ACTION_UP");
                b = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //    Log.e("MyListView", "ACTION_MOVE");
                float dY = ev.getY() - mStartY;
                Log.e("ML", "dY:" + dY);
                int childCount = getChildCount();
                Log.e("ML", "count=" + childCount);

                View childLast = this.getChildAt(childCount - 1);
                View childFirst = this.getChildAt(0);

                Log.e("ML", childLast.getBottom() + "  :  " + this.getBottom());

                // 最后一个条目上划
                if (childLast.getBottom() <= this.getBottom() && dY < 0 && getLastVisiblePosition() >= getAdapter().getCount() - 1) {
                    requestDisallowInterceptTouchEvent(false);  // 交还事件
                }

                // 第一个条目下滑
                if (childFirst.getTop() >= this.getTop() && dY > 0 && getFirstVisiblePosition() == 0) {
                    requestDisallowInterceptTouchEvent(false);  // 交还事件
                }
                break;
        }
        //Log.e("MyListView","~0x80000:"+Integer.toHexString(~0x80000));
        mStartY = ev.getY();

        return b;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount > 0) {
            Log.e("ML", "has child : " + childCount);
            View child0 = getChildAt(0);
            if (child0 != null) {
                child0.measure(widthMeasureSpec, heightMeasureSpec); // 测量子view
                int measuredHeight = child0.getMeasuredHeight();
                Log.e("ML", "first child : " + measuredHeight);
                int height = 7 * measuredHeight;
                int measuredHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, measuredHeightSpec);
            }
        } else {
            int defaultChildHeight = 156;
            int measuredHeightSpec = MeasureSpec.makeMeasureSpec(defaultChildHeight * 6, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, measuredHeightSpec);

            Log.e("ML", "getMeasuredHeight : " + getMeasuredHeight());
        }
    }
}
