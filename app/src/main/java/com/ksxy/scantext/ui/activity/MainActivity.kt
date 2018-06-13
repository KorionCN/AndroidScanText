package com.ksxy.scantext.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.baidu.ocr.ui.camera.CameraActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.ksxy.scantext.R
import com.ksxy.scantext.core.OCRSevice
import com.ksxy.scantext.ui.dialog.DoingDialog
import com.ksxy.scantext.utils.SimpleUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream


class MainActivity : AppCompatActivity(), View.OnClickListener {

    //cdv_camera
    //cdv_fileMgr
    private var mImagePath: String? = null
    companion object {
        private const val REQUEST_IMAGE_CAPTURE =  0
        private const val REQUEST_IMAGE_SELECT =  1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cdv_camera.setOnClickListener(this)
        cdv_fileMgr.setOnClickListener(this)
    }

    private fun capturePicture(){
        if (!SimpleUtil.checkTokenStatus()){
            return
        }
        val file = SimpleUtil.createExternalCacheFile(baseContext)
        if(file != null){
            mImagePath = file.absolutePath
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, file.absolutePath)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } else {
            ToastUtils.showShort(resources.getString(R.string.create_file_fail))
        }
    }


    private fun selectFile(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        if(intent.resolveActivity(packageManager) != null){
            startActivityForResult(intent, REQUEST_IMAGE_SELECT)
        }
    }

    private fun postScanText() {
        window.decorView.post {
            requestScanText()
        }
    }

    private fun requestScanText(){
        DoingDialog.newInstance().withMessage("识别中...").show(supportFragmentManager, DoingDialog.TAG)
        OCRSevice.scanPicture(baseContext, mImagePath, object: OCRSevice.OnScanResultListener{
            override fun onResult(result: String?) {
                if(!TextUtils.isEmpty(result)){
                    ScanResultActivity.lauchActivity(this@MainActivity, result!!)
                }else{
                    ToastUtils.showShort("未识别出图片中的文本")
                }
            }

            override fun onError(message: String?) {
                ToastUtils.showShort("请求失败:${message}")
            }

            override fun onRequestOver() {
                DoingDialog.newInstance().dismiss()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cdv_camera -> {
                capturePicture()
            }
            R.id.cdv_fileMgr -> {
                selectFile()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_IMAGE_CAPTURE -> {
                if(resultCode == Activity.RESULT_OK){
                    postScanText()
                }
            }
            REQUEST_IMAGE_SELECT -> {
                if(resultCode == Activity.RESULT_OK){
                    val uri = data?.data
                    Observable.just(uri)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext {
                                DoingDialog.newInstance().withMessage("压缩图片中...").show(supportFragmentManager, DoingDialog.TAG)
                            }.observeOn(Schedulers.io())
                            .map(Function<Uri?, String> {
                                val fileDesc = contentResolver.openFileDescriptor(it, "r")
                                val bitmap = BitmapFactory.decodeFileDescriptor(fileDesc.fileDescriptor)
                                fileDesc.close()
                                val file = SimpleUtil.createExternalCacheFile(baseContext)
                                val r = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
                                file!!.absolutePath
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext {
                                DoingDialog.newInstance().dismiss()

                            }.subscribe{
                                mImagePath = it
                                postScanText()
                            }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private var lastBackPressedTime: Long = 0
    private val BACK_TO_EXIT_THRESHO = 1500   //1500毫秒
    override fun onBackPressed() {
        val time = System.currentTimeMillis()
        if(time - lastBackPressedTime > BACK_TO_EXIT_THRESHO){
            lastBackPressedTime = time
            ToastUtils.showShort(resources.getString(R.string.again_to_exit))
            return
        }
        super.onBackPressed()
    }
}

