package com.example.myapplication.infra

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {

    private val logginIntercetor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logginIntercetor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://aiko-olhovivo-proxy.aikodigital.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService = retrofit.create(ApiService::class.java)


}