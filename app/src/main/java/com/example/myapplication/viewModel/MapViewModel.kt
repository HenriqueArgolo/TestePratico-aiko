package com.example.myapplication.viewModel

import androidx.lifecycle.ViewModel
import com.example.myapplication.infra.Retrofit
import com.example.myapplication.model.BusStop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapViewModel: ViewModel() {

    private val AUTH_TOKEN = "3600262b8523ce1d9af06c00ccb91257d5b223124f748aee5fc80409107baf92"
    suspend fun getBusStop():List<BusStop>{
        val apiService = Retrofit().apiService
        return withContext(Dispatchers.IO){
            try {
              val response = apiService.getAllBusStop("Bearer $AUTH_TOKEN")
                response.map {
                    it
                }
            }catch (e: Exception){
                e.printStackTrace()
                emptyList()
            }
        }
    }

    private suspend fun getLines():List<String>{
        return withContext(Dispatchers.IO){
            try {
                val response = Retrofit().apiService.searchBusLine("Bearer $AUTH_TOKEN")
                response.lines.map {
                    it.lineCode
                }
            }catch (e: Exception){
                e.printStackTrace()
                emptyList()
            }
        }
    }
}