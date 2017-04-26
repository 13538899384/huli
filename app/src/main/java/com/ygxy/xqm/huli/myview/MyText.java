package com.ygxy.xqm.huli.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by XQM on 2017/3/22.
 */

public class MyText extends TextView{
    int lastX;
    int lastY;

    public MyText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离
                int offX = x - lastX;
                int offY = y - lastY;
                offsetLeftAndRight(offX);
                offsetTopAndBottom(offY);
                break;
        }
        return true;//记得返回true，说明被我们这里消化了改事件
    }
}
