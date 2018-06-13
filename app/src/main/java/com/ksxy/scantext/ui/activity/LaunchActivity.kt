package com.ksxy.scantext.ui.activity

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.WindowManager
import com.ksxy.scantext.R
import com.ksxy.scantext.utils.SimpleUtil
import com.yanzhenjie.permission.AndPermission
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class LaunchActivity : AppCompatActivity() {

    //tv_label;
    //tv_info;

    private companion object {
        val NECESSARY_PERMISSIOMS = arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    }

    private var mPermissionFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_launch)

        AndPermission.with(this)
                .runtime()
                .permission(NECESSARY_PERMISSIOMS)
                .rationale { context, data, executor ->
                    var permissionStr = SimpleUtil.transformPermissions2Text(this, data)
                    AlertDialog.Builder(this)
                            .setMessage("程序需要${permissionStr}权限,否则无法正常运行")
                            .setPositiveButton("授权"){
                                dialog, which -> executor.execute()
                            }.setNegativeButton("取消"){
                                dialog, which -> executor.cancel()
                            }
                }
                .onGranted {
                    mPermissionFlag = true
                    Observable.timer(2, TimeUnit.SECONDS).subscribe{
                        launchMainActivity()
                    }
                }.onDenied{
                    var permissionStr = SimpleUtil.transformPermissions2Text(this, it)
                    AlertDialog.Builder(this)
                            .setMessage("获取${permissionStr}权限失败, 程序将无法正常运行")
                            .setPositiveButton("确定") { dialog, which ->
                                finish()
                            }
                            .show()
                }
                .start()
    }

    private fun launchMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
