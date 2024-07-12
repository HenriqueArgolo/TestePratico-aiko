package com.example.myapplication.infra

import com.example.myapplication.model.BusArriveResponse
import com.example.myapplication.model.BusStop
import com.example.myapplication.model.BusStopSearch
import com.example.myapplication.model.Lines
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

import retrofit2.http.Query

interface ApiService {


    @POST("Login/Autenticar")
    suspend fun auth(@Query("token")token: String): Boolean
    @GET("Linha/Buscar")
    suspend fun searchBusLine(@Header("Authorization") token: String, @Query("termosBusca") termoBusca: String):List<Lines>

    @GET("Parada/Buscar?termosBusca=")
    suspend fun getAllBusStop(): List<BusStop>

    @GET("Parada/Buscar")
    suspend fun getBusStopByNameOrAddress(@Header("Authorization") token: String, @Query("termosBusca") termosBusca: String): List<BusStopSearch>


    @GET("Previsao/Parada")
    suspend fun getBusByBusStopCode(@Header("Authorization") token: String, @Query("codigoParada") codigoParada: String):BusArriveResponse
}