package com.ramagouda.screens.landing

import com.ramagouda.models.Base

interface LandingView {
    fun init()
    fun onSuccess(factList: Base)
    fun onError(error: String)
}