package com.example.myapplication.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.model.BusStop
import com.example.myapplication.viewModel.MapViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MapFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var icon: BitmapDescriptor
    private var busStopList: List<BusStop> = emptyList()
    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMap()

    }

    private fun setUpMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            mMap = googleMap
            initializeMap()
            fetchBusStop()
        }

    }

    private fun initializeMap() {
        val saoPaulo = LatLng(-23.55052, -46.633308)
        mMap.addMarker(
            MarkerOptions()
                .position(saoPaulo)
                .title("Marker Title")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 15f))
    }

    private fun findStop(busStopList: List<BusStop>) {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bus_stop2)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
        icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)

        busStopList.forEach { busStop ->
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(busStop.lat, busStop.long))
                    .title(busStop.stopLocation)
                    .icon(icon)
            )
        }

    }

    private fun fetchBusStop(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                busStopList = viewModel.getBusStop()
                activity?.runOnUiThread {
                    findStop(busStopList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun configLine() {
        val busRoute = listOf(
            LatLng(-23.534564, -46.654302),
            LatLng(-23.525799, -46.679251),
        )

        mMap.addPolyline(
            PolylineOptions()
                .addAll(busRoute)
                .width(5f)
                .color(R.color.black)
                .geodesic(true)

        )
    }
}
