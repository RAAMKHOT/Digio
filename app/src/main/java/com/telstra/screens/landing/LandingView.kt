package com.telstra.screens.landing

import com.telstra.models.Base

interface LandingView {
    fun init()
    fun onSuccess(factList: Base)
    fun onError(error: String)
}