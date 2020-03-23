package com.telstra.helper

import com.telstra.network.RestClient
import com.telstra.network.SOService

object CommonUtils {
    val BASE_URL = "https://dl.dropboxusercontent.com"
    fun getSOService(): SOService {
        return RestClient.getClient(BASE_URL)!!.create(SOService::class.java)
    }


}