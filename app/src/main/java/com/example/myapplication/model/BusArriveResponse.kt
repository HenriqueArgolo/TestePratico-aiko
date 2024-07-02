package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class BusArriveResponse(
    @SerializedName("p") val busStop: BusStop
)
