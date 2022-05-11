package com.ramagouda.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ramagouda.R
import com.ramagouda.helper.CommonUtils
import com.ramagouda.screens.activities.PdfListActivity
import kotlinx.android.synthetic.main.activity_pdf_list.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CommonUtils.verifyPermissions(this)

        val handler = Handler()
        handler.postDelayed({ openMainActivity() }, 4000)
    }

    private fun openMainActivity() {
        val intent = Intent(this@SplashActivity, PdfListActivity::class.java)
        startActivity(intent)
        finish()
    }
}
