package com.telstra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.telstra.helper.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.net.ssl.SSLHandshakeException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getFactData()
    }

    private fun getFactData() {
        val repository = CommonUtils.getSOService()

        CompositeDisposable().add(
            repository.getFacts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { result ->
                        if (result != null) {
                            
                        }
                    },
                    { error ->
                        if (error is SSLHandshakeException) {
                            Toast.makeText(
                                this,
                                "We are getting SSL Handshake Exception, We need to add SSL cert in Server side \n Please use the lower version device.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        )
    }
}
