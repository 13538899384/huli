package com.ygxy.xqm.huli.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.ygxy.xqm.huli.R;

/**
 * Created by XQM on 2017/3/14.
 */

public class DIYEditTextPWD extends EditText{
    /**
     * 位于控件内右侧密码是否可见
     */
    private Drawable mCloseDrawable;
    private Drawable mOpenDrawable;
    /**
     * 位于控件内左边的图片
     */
    private Drawable mLeftDrawable;
    public DIYEditTextPWD(Context context) {
        super(context);
        init();
    }

    public DIYEditTextPWD(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DIYEditTextPWD(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //设置背景
        setBackgroundResource(R.drawable.edittext_background);
        mLeftDrawable = getCompoundDrawables()[0];
        mLeftDrawable = getResources().getDrawable(R.drawable.login_pwd);
        mLeftDrawable.setBounds(0, 0,
                (int) (mLeftDrawable.getIntrinsicWidth() * 0.65),
                (int) (mLeftDrawable.getIntrinsicHeight() * 0.65));

        mCloseDrawable = getCompoundDrawables()[2];
        mCloseDrawable = getResources().getDrawable(R.drawable.pwd_close);
        mCloseDrawable.setBounds(0,0,(int)(mCloseDrawable.getIntrinsicWidth() * 0.65),
                (int)(mCloseDrawable.getIntrinsicHeight() * 0.65));

        mOpenDrawable = getCompoundDrawables()[2];
        mOpenDrawable = getResources().getDrawable(R.drawable.pwd_open);
        mOpenDrawable.setBounds(0, 0,
                (int) (mCloseDrawable.getIntrinsicWidth() * 0.65),
                (int) (mCloseDrawable.getIntrinsicHeight() * 0.65));

        setPwdDrawable(false);
        setLeftIconVisible();
    }

    private void setLeftIconVisible() {
        setCompoundDrawables(mLeftDrawable, getCompoundDrawables()[1],
                getCompoundDrawables()[2], getCompoundDrawables()[3]);
    }

    /**
     * 设置密码是否可见图标
     */
    private void setPwdDrawable(boolean visiable) {
        Drawable right = visiable ? mOpenDrawable : mCloseDrawable;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    if (this.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                        this.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        Editable etable = this.getText();
                        Selection.setSelection(etable, etable.length()); // 隐藏
                        setPwdDrawable(false);
                    } else {
                        this.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        Editable etable = this.getText();
                        Selection.setSelection(etable, etable.length()); // 显示
                        setPwdDrawable(true);
                        this.invalidate();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
