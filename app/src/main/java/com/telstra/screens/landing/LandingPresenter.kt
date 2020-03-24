package com.telstra.screens.landing

import com.telstra.helper.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.net.ssl.SSLHandshakeException

class LandingPresenter(private val view: LandingView) {
    private val repository = CommonUtils.getSOService()
    fun getFactData() {
        CompositeDisposable().add(
            repository.getFacts().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                    { result ->
                        if (result != null) {
                            view.onSuccess(result)
                        }
                    },
                    { error ->
                        if (error is SSLHandshakeException) {
                            view.onError("We are getting SSL Handshake Exception, We need to add SSL cert in Server side \n Please use the lower version device.")
                        } else {
                            view.onError("Somthing went wrong")
                        }
                    })
        )
    }

}
