package com.pecuyu.event_dispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * <br/>Author: pecuyu
 * <br/>Date: 2018/5/18
 * <br/>TODO:
 */

public class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        boolean b = super.onInterceptTouchEvent(ev);
        if (ev.getAction()== MotionEvent.ACTION_DOWN){
            //return true; // 拦截down事件
        }

        return b;
    }
}
