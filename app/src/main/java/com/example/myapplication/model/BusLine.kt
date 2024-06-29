package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class BusLine(
    @SerializedName("c") val lineCode: String,
    @SerializedName("cl") val lineId: Int,
    @SerializedName("sl") val direction: Int,
    @SerializedName("lt0") val destination: String,
    @SerializedName("lt1") val origin: String,
    @SerializedName("qv") val quantityVehicles: Int,
    @SerializedName("vs") val vehicles: List<BusVehicle>
)
