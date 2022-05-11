package com.ramagouda.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RestClient {
    private val CLIENT_ID = "AIZ67DUSNZ8TGWJV4DZ7DI3T5Z2LN2W2"
    private val CLIENT_SECRET = "ASN9AVKHU6HF41KTU71G3KNXLG1ET7BC"
    fun getClient(baseUrl: String): Retrofit? {

        val interceptor = Interceptor { chain ->
            val original: Request = chain.request()
            val request: Request = original.newBuilder()
                .header("Authorization", Credentials.basic(CLIENT_ID, CLIENT_SECRET))
                .method(original.method(), original.body())
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    }
}