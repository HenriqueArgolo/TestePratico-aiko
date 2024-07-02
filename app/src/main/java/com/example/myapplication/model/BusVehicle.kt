package com.example.myapplication.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class BusVehicle(
    @SerializedName("p") val busPrefix: String,
    @SerializedName("t") val arriveTime: String,
    @SerializedName("a") val active: Boolean,
    @SerializedName("ta") val updateTime: String,
    @SerializedName("py") val lat: Double,
    @SerializedName("px") val long: Double,
    @SerializedName("sv") val sv: String?,
    @SerializedName("is") val ls: String?
)
