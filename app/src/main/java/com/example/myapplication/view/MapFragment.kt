package com.example.myapplication.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.BusAdapter
import com.example.myapplication.databinding.FragmentMapBinding
import com.example.myapplication.model.BusLine
import com.example.myapplication.model.BusLineVehicle
import com.example.myapplication.model.BusStop
import com.example.myapplication.viewModel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap
    private val  currentStopCode = ""
    private lateinit var icon: BitmapDescriptor
    private var busLineVehicle: MutableList<BusLineVehicle> = mutableListOf()
    private lateinit var busAdapter: BusAdapter
    private var busStopList: List<BusStop> = emptyList()
    private val viewModel: MapViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        busAdapter = BusAdapter(requireContext(), mutableListOf())
        configBottomSheet()
        setUpMap()
        viewModel.listOfBus.observe(viewLifecycleOwner, Observer { busList ->
            busAdapter.setData(busList)
            busLineVehicle = busList
        })

    }


    // Sets up the Google Map
    private fun setUpMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            mMap = googleMap
            initializeMap() // Initialize the map with a marker and camera position
            fetchBusStop() // Fetch bus stop data
            mMap.setOnMapClickListener {
                binding.optionsContainer.visibility = View.GONE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }


    // Initializes the map with a default marker and camera position
    private fun initializeMap() {
        val saoPaulo = LatLng(-23.55052, -46.633308)
        mMap.addMarker(
            MarkerOptions()
                .position(saoPaulo)
                .title("Marker Title")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 15f))
    }

    private fun imageToBitMap(image: Int): BitmapDescriptor{
        val bitmap = BitmapFactory.decodeResource(resources, image)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
        icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
        return icon
    }

    // Adds bus stop markers to the map
    private fun findBusStop(busStopList: List<BusStop>) {
        icon = imageToBitMap(R.drawable.bus_stop2)

        busStopList.forEach { busStop ->
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(busStop.lat, busStop.long))
                    .title(busStop.stopLocation)
                    .icon(icon)
            )
            marker?.setBusStop(busStop)

        }
        mMap.setOnMarkerClickListener { clickedMarker ->
            val clickedBusStop = clickedMarker.getBusStop()

            if (clickedBusStop != null) {
                binding.busStopName.text = clickedBusStop.stopName
                binding.busStopAdress.text = clickedBusStop.stopLocation
                binding.optionsContainer.visibility = View.VISIBLE
                configRv()
                configclicks(clickedBusStop)
            }
            true
        }
    }


    // Configures the RecyclerView for displaying bus data
    private fun configRv() {
        binding.rv.apply {
            adapter = busAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    // Fetches bus stop data and updates the map with markers
    private fun fetchBusStop() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                busStopList = viewModel.getBusStop()
                activity?.runOnUiThread {
                    findBusStop(busStopList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Adds a polyline to the map representing a bus route
    private fun showBusinTheMap() {
        val icon = imageToBitMap(R.drawable.greenbus)
        busLineVehicle.forEach { bl ->
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(bl.vehicle.lat, bl.vehicle.long))
                    .title(bl.vehicle.busPrefix)
                    .icon(icon)
            )
        }
    }
    // Configures click listeners for buttons in the bottom sheet
    private fun configclicks(code: BusStop) {
        binding.busTime.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.fetchBusArriveTime(code.stopCode)
            }
            configRv()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.fab.visibility = View.VISIBLE
        }
        binding.linesRoute.setOnClickListener {

        }
        binding.favoriteBusStop.setOnClickListener {

        }
        binding.fab.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.fetchBusArriveTime(code.stopCode)
            }
            showBusinTheMap()
        }
    }

    // Configures the bottom sheet behavior
    private fun configBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomsheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = 0
    }

    // Sets a bus stop as the tag of a marker
    private fun Marker.setBusStop(busStop: BusStop) {
        tag = busStop
    }

    // Gets the bus stop from the tag of a marker
    private fun Marker.getBusStop(): BusStop? {
        return tag as? BusStop
    }
}
