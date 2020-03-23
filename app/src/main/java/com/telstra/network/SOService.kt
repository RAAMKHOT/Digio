package com.telstra.network

import com.telstra.models.Base
import retrofit2.http.GET
import retrofit2.http.Headers
import io.reactivex.Observable

interface SOService {
    @Headers("Content-Type: application/json")
    @GET("/s/2iodh4vg0eortkl/facts.json")
    fun getFacts(): Observable<Base>
}