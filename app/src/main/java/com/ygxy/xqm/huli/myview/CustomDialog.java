package com.ygxy.xqm.huli.myview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.ygxy.xqm.huli.R;

/**
 * Created by XQM on 2017/4/16.
 */

public class CustomDialog extends ProgressDialog{
    private TextView mTvMessage;
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.load_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

    }

//    public void setMessage(CharSequence message){
//        if(message.length() > 0){
//            findViewById(R.id.tv_load_dialog).setVisibility(View.VISIBLE);
//            mTvMessage = (TextView) findViewById(R.id.tv_load_dialog);
//            mTvMessage.setText(message);
//            mTvMessage.invalidate();
//        }
//
//    }

    @Override
    public void show() {
        super.show();
    }


}
