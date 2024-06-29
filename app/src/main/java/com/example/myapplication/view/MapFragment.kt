package com.example.myapplication.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var icon: BitmapDescriptor
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findBus()
        configLine()
    }

    private fun findBus(){
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bus2)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
        icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val saoPaulo = LatLng(-23.55052, -46.633308)
        mMap.addMarker(
            MarkerOptions()
                .position(saoPaulo)
                .icon(icon)
                .title("Bus Location /n LSDFASLÇDFLS /n asdhAJFSDF")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 15f))
    }

    private fun configLine(){
        val busRoute = listOf(
            LatLng(-23.55052, -46.633308),
            LatLng(-23.5555, -46.6400),
            LatLng(-23.5600, -46.6500)
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


