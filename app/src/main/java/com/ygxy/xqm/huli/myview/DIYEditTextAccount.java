package com.ygxy.xqm.huli.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ygxy.xqm.huli.R;

/**
 * Created by XQM on 2017/3/14.
 */

public class DIYEditTextAccount extends EditText implements TextWatcher{
    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;
    /**
     * 位于控件内清除EditText内容的图片，也就是右边图片
     */
    private Drawable mClearDrawable;

    /**
     * 位于控件内左侧的图片
     */
    private Drawable mLeftDrawable;

    private DrawableLeftListener mLeftListener ;
    private DrawableRightListener mRightListener ;

    public interface DrawableLeftListener {
        public void onDrawableLeftClick(View view) ;
    }

    public void setDrawableLeftListener(DrawableLeftListener listener) {
        this.mLeftListener = listener;
    }

    public void setDrawableRightListener(DrawableRightListener listener) {
        this.mRightListener = listener;
    }
    public interface DrawableRightListener {
        public void onDrawableRightClick(View view) ;
    }

    public DIYEditTextAccount(Context context) {
        super(context);
        init();
    }


    public DIYEditTextAccount(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);
        init();
    }

    public DIYEditTextAccount(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //设置背景
        setBackgroundResource(R.drawable.edittext_background);
        mLeftDrawable = getCompoundDrawables()[0];//获取张图片
        mLeftDrawable = getResources().getDrawable(R.drawable.login_account_icon);
        //设置位置
        mLeftDrawable.setBounds(0, 0,
                (int) (mLeftDrawable.getIntrinsicWidth() * 0.65),
                (int) (mLeftDrawable.getIntrinsicHeight() * 0.65));

        mClearDrawable = getCompoundDrawables()[2];
        mClearDrawable = getResources().getDrawable(R.drawable.clear);
        mClearDrawable.setBounds(0,0,(int)(mClearDrawable.getIntrinsicWidth() * 0.65)
                ,(int) (mClearDrawable.getIntrinsicHeight() * 0.65));
        setClearDrawable();
        setLeftIconVisible();
        addTextChangedListener(this);
    }

    /**
     * 设置左侧图标，调用setCompoundDrawables为EditText绘制上去
     *
     * @param
     */
    private void setLeftIconVisible() {
        setCompoundDrawables(mLeftDrawable, getCompoundDrawables()[1],
                getCompoundDrawables()[2], getCompoundDrawables()[3]);
    }

    //设置删除图片
    private void setClearDrawable() {
        if (length() < 1){
            setClearIconVisible(false);
        }else {
            setClearIconVisible(true);
        }
    }

    /**
     *设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    private void setClearIconVisible(boolean visible) {
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], visible ? mClearDrawable : null,
                getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore,
                              int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setClearDrawable();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        setClearDrawable();
    }

    /**
     * 点击删除按钮，清理内容
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);
            this.requestFocus();

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
        }
        return super.onTouchEvent(event);
    }
}
