package com.example.myapplication.infra

import com.example.myapplication.model.BusArriveResponse
import com.example.myapplication.model.BusStop
import com.example.myapplication.model.BusStopSearch
import com.example.myapplication.model.PositionResponse
import retrofit2.http.GET
import retrofit2.http.Header

import retrofit2.http.Query

interface ApiService {

    @GET("Posicao")
    suspend fun searchBusLine(@Header("Autorzation") token: String):PositionResponse

    @GET("Parada/Buscar?termosBusca=")
    suspend fun getAllBusStop(@Header("Autorzation") token: String): List<BusStop>

    @GET("Parada/Buscar")
    suspend fun getBusStopByNameOrAddress(@Header("Autorzation") token: String, @Query("termosBusca") termosBusca: String): List<BusStopSearch>


    @GET("Previsao/Parada")
    suspend fun getBusByBusStopCode(@Header("Autorzation") token: String, @Query("codigoParada") codigoParada: String):BusArriveResponse
}