package com.ksxy.scantext.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ksxy.scantext.R
import kotlinx.android.synthetic.main.activity_scan_result.*

class ScanResultActivity : AppCompatActivity() {


    companion object {
        val KEY_SCAN_RESULT = "KEY_SCAN_RESULT"

        fun lauchActivity(activity: Activity, result: String){

            val intent = Intent(activity, ScanResultActivity::class.java)
            intent.putExtra(KEY_SCAN_RESULT, result)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val result = intent.getStringExtra(KEY_SCAN_RESULT)
        edt_result.setText(result)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
