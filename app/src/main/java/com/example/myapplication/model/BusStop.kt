package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class BusStop(
    @SerializedName("cp") val stopCode: String,
    @SerializedName("np") val stopName: String,
    @SerializedName("ed") val stopLocation: String,
    @SerializedName("py") val lat: Double,
    @SerializedName("px") val long: Double,
    @SerializedName("l") val line: List<BusLine>


)
