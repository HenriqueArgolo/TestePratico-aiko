package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.LinesObjectBinding
import com.example.myapplication.model.BusLineVehicle


class LineAdapter(
    private val context: Context,
    private var busVehicle: MutableList<BusLineVehicle> = mutableListOf()
) : RecyclerView.Adapter<LineAdapter.ViewHolder>() {

    class ViewHolder(private val binding: LinesObjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(busVehicle: BusLineVehicle) {
            binding.busCodeName.text = busVehicle.line.lineSign
            binding.destiny.text = busVehicle.line.destination
            binding.destiny.text = String.format("Destino: ${busVehicle.line.destination}")
            binding.origin.text = String.format("Origem: ${busVehicle.line.origin}")
            binding.quantity.text = String.format("Quantidade: ${busVehicle.line.vehicleCount} veiculos")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LinesObjectBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vehicle = busVehicle[position]
        holder.bind(vehicle)
    }

    override fun getItemCount(): Int {
        return busVehicle.size
    }

    fun setData(newList: List<BusLineVehicle>) {
        busVehicle.clear()
        busVehicle.addAll(newList)
        notifyDataSetChanged()
    }


}
