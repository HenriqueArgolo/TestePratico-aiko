package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.LinesObjectBinding
import com.example.myapplication.model.BusLine
import com.example.myapplication.model.BusLineVehicle




class LineAdapter(
    private val context: Context,
    private var busVehicle: MutableList<ListItem> = mutableListOf()
) : RecyclerView.Adapter<LineAdapter.ViewHolder>() {

    class ViewHolder(private val binding: LinesObjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: ListItem) {
            when(listItem){
                is ListItem.BusLinevehicle->{
                    binding.busCodeName.text = listItem.vehicle.line.lineSign
                    binding.destiny.text = listItem.vehicle.line.destination
                    binding.destiny.text = String.format("Destino: ${listItem.vehicle.line.destination}")
                    binding.origin.text = String.format("Origem: ${listItem.vehicle.line.origin}")
                    binding.quantity.text = String.format("Quantidade: ${listItem.vehicle.line.vehicleCount} veiculos")
                }
                is ListItem.Line ->{
                    binding.busCodeName.text = listItem.line.lineSign
                    binding.destiny.text = listItem.line.destination
                    binding.destiny.text = String.format("Destino: ${listItem.line.destination}")
                    binding.origin.text = String.format("Origem: ${listItem.line.origin}")
                    binding.quantity.text = String.format("Quantidade: ${listItem.line.vehicleCount} veiculos")
                }

                is ListItem.Line -> TODO()
            }
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

    fun setData(newList: List<ListItem>) {
        busVehicle.clear()
        busVehicle.addAll(newList)
        notifyDataSetChanged()
    }


}
