package com.example.myapplication.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class BusVehicle(
    @SerializedName("p") val prefix: Int,
    @SerializedName("a") val accessible: Boolean,
    @SerializedName("ta") val timestamp: Date,
    @SerializedName("py") val latitude: Double,
    @SerializedName("px") val longitude: Double,
    @SerializedName("sv") val status: String?,
    @SerializedName("is") val additionalInfo: String?
)
