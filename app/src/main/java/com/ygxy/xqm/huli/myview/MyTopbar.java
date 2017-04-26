package com.ygxy.xqm.huli.myview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ygxy.xqm.huli.R;

/**
 * Created by XQM on 2017/3/19.
 */

public class MyTopbar extends LinearLayout{
    private TextView mTvMainTitleLeft;
    private TextView mTvMainTitle;
    private TextView mTvMainTitleRight;
    private DrawableLeftListener mLeftListener;
    private DrawableRightListener mRightListener;
    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;
    private final Boolean visible = true;
    public interface DrawableLeftListener{
        public void onDrawableLeftClick(View view);
    }
    public interface DrawableRightListener {
        public void onDrawableRightClick(View view);
    }
    public MyTopbar(Context context) {
        super(context);
    }

    public void setDrawableLeftListener(DrawableLeftListener listener) {
        this.mLeftListener = listener;
    }

    public void setDrawableRightListener(DrawableRightListener listener) {
        this.mRightListener = listener;
    }

    public MyTopbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.fragmenttitlebar,this);
        mTvMainTitleLeft = (TextView) findViewById(R.id.lt_main_title_left);
        mTvMainTitle = (TextView) findViewById(R.id.lt_main_title);
        mTvMainTitleRight = (TextView) findViewById(R.id.lt_main_title_right);
    }

    public MyTopbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置主title的内容
     */
    public void setMainTitle(String text){
        mTvMainTitle.setVisibility(VISIBLE);
        mTvMainTitle.setText(text);
    }

    /**
     *设置主标题的内容颜色
     * @param color
     */
    public void setMainTitleColor(int color){
        mTvMainTitle.setTextColor(color);
    }

    /**
     * 设置左边的标题
     * @param text
     */
    public void setMainTitleLeftText(String text) {
        mTvMainTitleLeft.setVisibility(View.VISIBLE);
        mTvMainTitleLeft.setText(text);
    }

    /**
     * 设置左边标题的颜色
     * @param color
     */
    public void setMainTitleLeftColor(int color){
        mTvMainTitleLeft.setTextColor(color);
    }

    /**
     * 设置左边图标
     * @param res
     */
    public void setMainTitleLeftDrawable(int res){
        Drawable dwLeft = ContextCompat.getDrawable(getContext(),res);
        dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
        mTvMainTitleLeft.setCompoundDrawables(dwLeft,null,null,null);
    }

    /**
     * 设置右边的标题
     * @param text
     */
    public void setMainTitleRightText(String text){
        mTvMainTitleRight.setVisibility(VISIBLE);
        mTvMainTitleRight.setText(text);
    }

    /**
     * 设置右边文字的颜色
     * @param color
     */
    public void setMainTitleRightTextColor(int color){
        mTvMainTitleRight.setTextColor(color);
    }

    /**
     * 设置右边的图标
     * @param res
     */
    public void setMainTitleRightDrawable(int res){
        Drawable dwRight = ContextCompat.getDrawable(getContext(),res);
        dwRight.setBounds(0, 0, dwRight.getMinimumWidth(), dwRight.getMinimumHeight());
        mTvMainTitleRight.setCompoundDrawables(null, null, dwRight, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mRightListener != null) {
                    Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT] ;
                    if (drawableRight != null && event.getRawX() >= (getRight() - drawableRight.getBounds().width())) {
                        mRightListener.onDrawableRightClick(this) ;
                        return true ;
                    }
                }
                if (mLeftListener != null) {
                    Drawable drawableLeft = getCompoundDrawables()[DRAWABLE_LEFT] ;
                    if (drawableLeft != null && event.getRawX() <= (getLeft() + drawableLeft.getBounds().width())) {
                        mLeftListener.onDrawableLeftClick(this) ;
                        return true ;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    public Drawable[] getCompoundDrawables() {
        throw new RuntimeException("Stub!");
    }
}
