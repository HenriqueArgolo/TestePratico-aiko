package com.example.myapplication.infra

import android.telecom.Call
import com.example.myapplication.model.BusStop
import com.example.myapplication.model.PositionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @GET("Posicao")
    suspend fun searchBusLine(@Header("Autorzation") token: String):PositionResponse


    @GET("Parada/Buscar?termosBusca=")
    suspend fun getAllBusStop(@Header("Autorzation") token: String): List<BusStop>
}