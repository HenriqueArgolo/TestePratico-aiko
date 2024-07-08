package com.example.myapplication.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.BusAdapter
import com.example.myapplication.adapter.LineAdapter
import com.example.myapplication.adapter.ListItem
import com.example.myapplication.databinding.FragmentMapBinding
import com.example.myapplication.model.BusLineVehicle
import com.example.myapplication.model.BusStop
import com.example.myapplication.model.BusStopSearch
import com.example.myapplication.viewModel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


@Suppress("DEPRECATION")
class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap
    private var num = 0
    private val busMarkers = mutableMapOf<String, Marker>()
    private var stopList: List<BusStopSearch> = emptyList()
    private lateinit var icon: BitmapDescriptor
    private var busLineVehicle: MutableList<BusLineVehicle> = mutableListOf()
    private lateinit var busAdapter: BusAdapter
    private lateinit var lineAdapter: LineAdapter
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
        lineAdapter = LineAdapter(requireContext(), mutableListOf())
        configBottomSheet()
        setUpMap()

        // atualizo os dados do recyclerview
        viewModel.listOfBus.observe(viewLifecycleOwner, Observer { busList ->
            busAdapter.setData(busList)
<<<<<<< HEAD
            val filterLine =  busList.distinctBy {  it.line.lineSign  }
            val lineList = filterLine.map { ListItem.BusLinevehicle(it) }
            lineAdapter.setData(lineList)
=======
            val lineList=  busList.distinctBy {  it.line.lineSign  }
            lineAdapter.setData(lineList )
>>>>>>> e0e77400ce07b8539860bb8ad25057658b671c62
            busLineVehicle = busList
        })

        initSearchContainer()
        searchBusStopBy()
    }


    // Configura o Google Map e alguns marcadores (aqui eu fugi feio das boas praticas, desculpa.)
    private fun setUpMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            mMap = googleMap
            initializeMap() // Inicializa o mapa com um marcador e posição da câmera
            fetchBusStop() // Busca dados de paradas de ônibus
            mMap.setOnMapClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            mMap.setOnMarkerClickListener { clickedMarker ->
                val clickedBusStop = clickedMarker.getBusStop()
                val busInfo = clickedMarker.getBusMarker()
                if (clickedBusStop != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.fetchBusArriveTime(clickedBusStop.stopCode)
                    }
                    configRv()
                    configRvLine()
                    binding.busStopName.text = clickedBusStop.stopName
                    binding.busStopAdress.text = clickedBusStop.stopLocation
                    binding.optionsContainer.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                    binding.searchContainer.visibility = View.GONE
                    configclicks(clickedBusStop)
                    num = 0
                }

                if (busInfo != null) {
                    busDialog(busInfo)
                }
                true
            }
        }
    }


    // Inicializa o mapa com um marcador padrão e posição da câmera
    private fun initializeMap() {
        val saoPaulo = LatLng(-23.55052, -46.633308)
        mMap.addMarker(
            MarkerOptions()
                .position(saoPaulo)
                .title("Marker Title")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 15f))
    }

    // Foca a câmera na primeira parada de ônibus da lista
    private fun focusCameraOnBusStop(busStops: List<BusStopSearch>) {
        if (busStops.isNotEmpty()) {
            val firstBusStop = busStops.first()
            val latLng = LatLng(firstBusStop.lat, firstBusStop.long)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
        } else {
            Toast.makeText(requireContext(), "$stopList", Toast.LENGTH_LONG).show()
        }
    }

    // Converte um recurso de imagem para um BitmapDescriptor
    private fun imageToBitMap(image: Int): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(resources, image)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
        icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)
        return icon
    }

    // Adiciona marcadores de paradas de ônibus ao mapa
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
    }

    // Configura o RecyclerView para exibir dados de ônibus
    private fun configRv() {
        binding.rv.apply {
            adapter = busAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun configRvLine() {
        binding.rvLines.apply {
            adapter = lineAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    // Busca dados de paradas de ônibus e atualiza o mapa com marcadores
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

    // Adiciona marcadores ao mapa representando ônibus em uma rota
    private fun showBusinTheMap() {
        val icon = imageToBitMap(R.drawable.greenbus)

        busLineVehicle.forEach { bl ->
            val position = LatLng(bl.vehicle.lat, bl.vehicle.long)
            val busPrefix = bl.vehicle.busPrefix

            if (busMarkers.containsKey(busPrefix)) {
                busMarkers[busPrefix]?.position = position
            } else {
                val busInfo = mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title("Bus Prefix: $busPrefix")
                        .snippet("Additional Info: ${bl.vehicle.arriveTime}")
                        .icon(icon)
                )
                if (busInfo != null) {
                    busMarkers[busPrefix] = busInfo
                }
                busInfo?.setBusMarker(bl)
            }
        }
    }

    // Exibe um diálogo com informações do ônibus
    private fun busDialog(bl: BusLineVehicle) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Informações do ônibus")
            setMessage(
                "Letreiro: ${bl.line.lineSign}\n" +
                        "Codigo da linha: ${bl.line.lineCode}\n" +
                        "Prefixo: ${bl.vehicle.busPrefix}\n" +
                        "Previsão de chegada: ${bl.vehicle.arriveTime}\n" +
                        "Destino: ${bl.line.destination}"
            )
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        }
        alertDialogBuilder.create().show()
    }

    // Configura listeners de clique para vários elementos da UI
    private fun configclicks(code: BusStop) {
        binding.busTime.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.fetchBusArriveTime(code.stopCode)
            }
            configRv()
            binding.rvLines.visibility = View.GONE
            binding.rv.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.linesRoute.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.fetchBusArriveTime(code.stopCode)
            }
            configRvLine()
            binding.rv.visibility = View.GONE
            binding.rvLines.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.favoriteBusStop.setOnClickListener {

        }
        binding.fab.setOnClickListener {
            busMarkers.values.forEach { it.remove() }
            busMarkers.clear()
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.fetchBusArriveTime(code.stopCode)
            }
            showBusinTheMap()
        }
    }

    // Configura o botão de busca de parada de ônibus
    private fun searchBusStopBy() {
        binding.searchButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val result = viewModel.getByTermoBusca(binding.searchInput.text.toString())
                focusCameraOnBusStop(result)
            }
        }
    }

    // Configura o comportamento do bottom sheet
    private fun configBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomsheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = 0
    }

    private fun initSearchContainer() {
        binding.searchBtn.setOnClickListener {
            if (num == 0) {
                binding.searchContainer.visibility = View.VISIBLE
                binding.optionsContainer.visibility = View.GONE
                num = 1
            } else {
                binding.searchContainer.visibility = View.GONE
                num = 0
            }
        }
    }

    // Define uma parada de ônibus como a tag de um marcador
    private fun Marker.setBusStop(busStop: BusStop) {
        tag = busStop
    }

    // Obtém a parada de ônibus da tag de um marcador
    private fun Marker.getBusStop(): BusStop? {
        return tag as? BusStop
    }

    // Define um ônibus como a tag de um marcador
    private fun Marker.setBusMarker(busStop: BusLineVehicle) {
        tag = busStop
    }

    // Obtém o ônibus da tag de um marcador
    private fun Marker.getBusMarker(): BusLineVehicle? {
        return tag as? BusLineVehicle
    }
}
