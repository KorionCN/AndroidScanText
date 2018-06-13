package com.ksxy.scantext;

import android.app.Application;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.ksxy.scantext.utils.GlobalConfig;

/**
 * author : YYCHEN
 * e-mail : xxx@xx
 * time   : 2018/06/01
 * desc   :
 * version: 1.0
 */
public class InitApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        initOCRAccessToken();
    }

    private void initOCRAccessToken() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                GlobalConfig.hasGotOCRToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
            }
        }, getApplicationContext(),  "百度API KEY", "对应的Secret Key");
    }


}
