package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.BusObjectBinding
import com.example.myapplication.model.BusLineVehicle
import java.text.SimpleDateFormat
import java.util.Locale


class BusAdapter(
    private val context: Context,
    private var busVehicle: MutableList<BusLineVehicle> = mutableListOf()
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
        val vehicle = busVehicle[position]
        holder.bind(vehicle)


    }

    override fun getItemCount(): Int {
        return busVehicle.size
    }

    fun setData(newList: List<BusLineVehicle>) {
        busVehicle.clear()
        busVehicle.addAll(newList.sortedBy { it.vehicle.arriveTime.toTimeMillis() })
        notifyDataSetChanged()
    }

    private fun String.toTimeMillis(): Long {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return try {
            format.parse(this)?.time ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

}