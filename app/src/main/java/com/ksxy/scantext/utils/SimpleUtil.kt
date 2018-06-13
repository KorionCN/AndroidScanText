package com.ksxy.scantext.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.ksxy.scantext.R
import com.yanzhenjie.permission.FileProvider
import com.yanzhenjie.permission.Permission
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DateFormat

/**
 * author : YYCHEN
 * e-mail : xxx@xx
 * time   : 2018/06/02
 * desc   :
 * version: 1.0
 */
class SimpleUtil{


    companion object {
        fun createExternalImageFile(context: Context): File {
            val timeStamp = TimeUtils.getNowString()
            val fileDir = context.getExternalFilesDir("images")
            val fileName = "JPEG_${timeStamp}"
            val resutl = File.createTempFile(fileName, ".jpg", fileDir)
            return resutl
        }

        fun createExternalCacheFile(context: Context): File?{
            val timeStamp = TimeUtils.getNowMills()
            val fileDir = context.externalCacheDir
            return File.createTempFile(timeStamp.toString(), "", fileDir)
        }

         fun getUriForFile(context: Context, file: File): Uri{
            return FileProvider.getUriForFile(context, "com.ksxy.scantext.fileprovider", file)
        }

        fun checkTokenStatus(): Boolean{
            if(!GlobalConfig.hasGotOCRToken){
                ToastUtils.showShort(R.string.no_init_ocr)
                return false
            }
            return true
        }

        fun transformPermissions2Text(context: Context, permissions: List<String>): String{
            val permissions = Permission.transformText(context, permissions)
            val sb = StringBuilder()
            for (one in  permissions){
                sb.append("${one}、")
            }
            sb.deleteCharAt(sb.lastIndex)
            return sb.toString()
        }

    }
}
