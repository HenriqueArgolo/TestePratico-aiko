package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.BusObjectBinding
import com.example.myapplication.model.BusLineVehicle


class BusAdapter(
    private val context: Context,
    private var busVehicle: List<BusLineVehicle>?
) : RecyclerView.Adapter<BusAdapter.ViewHolder>() {

    class ViewHolder(private val binding: BusObjectBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(busVehicle: BusLineVehicle) {
            binding.busCodeName.text = busVehicle.line.lineSign
            binding.destiny.text = busVehicle.line.destination
            binding.busPrefix.text = String.format("Prefixo:${busVehicle.vehicle.busPrefix}")
            binding.arriveTime.text = busVehicle.vehicle.arriveTime
        }



    }

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val binding = BusObjectBinding.inflate(inflater, parent, false)
    return ViewHolder(binding)
}


override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val vehicle = busVehicle?.get(position)
    if (vehicle != null) {
        holder.bind(vehicle)
    }

}

override fun getItemCount(): Int {
    return busVehicle?.size ?:0
}

}