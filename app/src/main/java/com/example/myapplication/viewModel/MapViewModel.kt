package com.example.myapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.infra.Retrofit
import com.example.myapplication.model.BusLineVehicle
import com.example.myapplication.model.BusStop
import com.example.myapplication.model.BusStopSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel : ViewModel() {

    private val _listOfBus = MutableLiveData<MutableList<BusLineVehicle>>()
    val listOfBus: LiveData<MutableList<BusLineVehicle>> get() =_listOfBus

    private val AUTH_TOKEN = "3600262b8523ce1d9af06c00ccb91257d5b223124f748aee5fc80409107baf92"


    init {
        _listOfBus.value = mutableListOf()
    }
    // Busca todas as paradas de ônibus
    suspend fun getBusStop(): List<BusStop> {
        val apiService = Retrofit().apiService
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllBusStop("Bearer $AUTH_TOKEN")
                response.map {
                    it
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // Busca paradas de ônibus por nome ou endereço
    suspend fun getByTermoBusca(nameOrAdress: String): List<BusStopSearch> {
        val apiService = Retrofit().apiService
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getBusStopByNameOrAddress("Bearer $AUTH_TOKEN", nameOrAdress)
                response.map {
                    it
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // Busca códigos de linhas de ônibus
    private suspend fun getLines(): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = Retrofit().apiService.searchBusLine("Bearer $AUTH_TOKEN")
                response.lines.map {
                    it.lineCode
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // Busca horários de chegada de ônibus para uma parada específica
    suspend fun fetchBusArriveTime(stopCode: String) {
        viewModelScope.launch {
            val busArriveTimeList = getBusArriveTime(stopCode)
            _listOfBus.value = busArriveTimeList.toMutableList()
        }
    }

    // Obtém os horários de chegada dos ônibus para um código de parada específico
    private suspend fun getBusArriveTime(busStopCode: String): List<BusLineVehicle> {
        return withContext(Dispatchers.IO) {
            try {
                val response = Retrofit().apiService.getBusByBusStopCode("Bearer $AUTH_TOKEN", busStopCode)
                response.busStop.line.flatMap { line ->
                    line.vehicles.map { vehicle ->
                        BusLineVehicle(line, vehicle)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
