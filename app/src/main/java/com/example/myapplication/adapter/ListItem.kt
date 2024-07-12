package com.example.myapplication.adapter

import com.example.myapplication.model.BusLine
import com.example.myapplication.model.BusLineVehicle
import com.example.myapplication.model.Lines

sealed class ListItem {
    data class BusLinevehicle(val vehicle: BusLineVehicle) : ListItem()
    data class Line(val line: Lines) : ListItem()
}