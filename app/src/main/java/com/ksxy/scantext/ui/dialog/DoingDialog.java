package com.ksxy.scantext.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ksxy.scantext.R;

/**
 * author : YYCHEN
 * e-mail : xxx@xx
 * time   : 2018/05/20
 * desc   :
 * version: 1.0
 */
public class DoingDialog extends DialogFragment {

    private static DoingDialog instance = null;
    public static final String TAG = "DoingDialog";

    TextView tv_message;
    private String message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_doing, container, false);
        tv_message = view.findViewById(R.id.tv_message);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setDimAmount(0);
        getDialog().getWindow().setBackgroundDrawable(null);
        setCancelable(false);
        tv_message.setText(message);

    }
    public DoingDialog withMessage(String message){
        this.message = message;
        return this;
    }

    public static DoingDialog newInstance(){
        if(instance == null){
            synchronized (DoingDialog.class){
                if(instance == null){
                    instance = new DoingDialog();
                }
            }
        }
        return instance;
    }

}
