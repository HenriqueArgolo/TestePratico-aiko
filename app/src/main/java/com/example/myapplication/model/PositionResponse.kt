package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class PositionResponse(
    @SerializedName("l") val lines: List<BusLine>
)
