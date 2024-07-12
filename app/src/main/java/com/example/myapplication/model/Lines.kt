package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class Lines(
    @SerializedName("cl") val lineCode: String,
    @SerializedName("lc") val isCircular: Boolean,
    @SerializedName("lt") val lineNumberPrefix: String,
    @SerializedName("sl") val direction: String,
    @SerializedName("tl") val lineNumberSuffix: String,
    @SerializedName("tp") val destinationTowardsPrimary: String,
    @SerializedName("ts") val destinationTowardsSecondary: String
)
