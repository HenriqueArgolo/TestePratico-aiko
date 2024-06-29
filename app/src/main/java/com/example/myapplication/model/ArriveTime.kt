package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class ArriveTime(
    @SerializedName("hr") val hour: String,
    @SerializedName("l") val lines: List<BusLine>
)
