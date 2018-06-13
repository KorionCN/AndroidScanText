package com.ksxy.scantext.core;

import android.content.Context;
import android.util.Log;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;

/**
 * author : YYCHEN
 * e-mail : xxx@xx
 * time   : 2018/06/02
 * desc   :
 * version: 1.0
 */
public class OCRSevice {

    public interface OnScanResultListener{
        void onRequestOver();
        void onResult(String result);
        void onError(String message);
    }

    public static void scanPicture(Context context, String filePath, final OnScanResultListener listener){
        GeneralParams params = new GeneralParams();
        params.setDetectDirection(true);
        params.setVertexesLocation(true);
        params.setRecognizeGranularity(GeneralParams.GRANULARITY_SMALL);

        params.setImageFile(new File(filePath));
        OCR.getInstance(context).recognizeGeneral(params, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult generalResult) {
                StringBuilder sb = new StringBuilder();
                for(WordSimple word: generalResult.getWordList()){
                    sb.append(word.getWords());
                    sb.append(" ");
                }
                listener.onRequestOver();
                listener.onResult(sb.toString());
            }

            @Override
            public void onError(OCRError ocrError) {
                listener.onRequestOver();
                listener.onError(ocrError.getMessage());
            }
        });
    }
}
