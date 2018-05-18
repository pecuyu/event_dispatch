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
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    float startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        boolean b = super.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("MyListView", "ACTION_DOWN");
                requestDisallowInterceptTouchEvent(true);
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("MyListView", "ACTION_UP");
                b = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //    Log.e("MyListView", "ACTION_MOVE");
                float dY = ev.getY() - startY;
                Log.e("ML", "dY:" + dY);
                int childCount = getChildCount();
                Log.e("ML", "count=" + childCount);

                View childAt = this.getChildAt(childCount-1);

                Log.e("ML",childAt.getBottom()+"  :  "+ this.getBottom());

                if (childAt.getBottom() <= this.getBottom() && dY < 0 && getLastVisiblePosition() >= getAdapter().getCount() - 1) {
                    requestDisallowInterceptTouchEvent(false);
                }

                break;
        }
        //Log.e("MyListView","~0x80000:"+Integer.toHexString(~0x80000));
        startY = ev.getY();

        return b;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        View child0 = getChildAt(0);
//        child0.measure(widthMeasureSpec,heightMeasureSpec);
        int childHeight = 100;
        //int measuredHeight = MeasureSpec.makeMeasureSpec(1 << 30 - 1, MeasureSpec.AT_MOST);
        int measuredHeight = MeasureSpec.makeMeasureSpec(childHeight * 7, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, measuredHeight);
    }
}
