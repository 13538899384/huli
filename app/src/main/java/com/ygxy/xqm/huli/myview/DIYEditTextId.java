package com.ygxy.xqm.huli.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.ygxy.xqm.huli.R;

/**
 * Created by XQM on 2017/3/14.
 */

public class DIYEditTextId extends EditText implements TextWatcher{
    private Drawable mLeftDrawable;
    private Drawable mClearDrawable;
    public DIYEditTextId(Context context) {
        super(context);
        init();
    }

    public DIYEditTextId(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DIYEditTextId(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //设置背景
        setBackgroundResource(R.drawable.edittext_background);
        mLeftDrawable = getCompoundDrawables()[0];
        mLeftDrawable = getResources().getDrawable(R.drawable.icon_id);

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

    private void setLeftIconVisible() {
        setCompoundDrawables(mLeftDrawable, getCompoundDrawables()[1],
                getCompoundDrawables()[2], getCompoundDrawables()[3]);
    }

    private void setClearDrawable() {
        if (length() < 1){
            setClearIconVisible(false);
        }else {
            setClearIconVisible(true);
        }
    }

    private void setClearIconVisible(boolean visible) {
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], visible ? mClearDrawable : null,
                getCompoundDrawables()[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        setClearDrawable();
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
        }
        return super.onTouchEvent(event);
    }
}
