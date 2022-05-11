package com.ramagouda.network

import com.example.example.UploadPdf
import com.ramagouda.models.Base
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface SOService {
    @Headers("Content-Type: application/json")
    @GET("/s/2iodh4vg0eortkl/facts.json")
    fun getFacts(): Observable<Base>

    @Headers("Content-Type: application/json")
    @POST("/v2/client/document/uploadpdf")
    fun uploadPdf(@Body body: UploadPdf): Observable<Response<Void>>
}