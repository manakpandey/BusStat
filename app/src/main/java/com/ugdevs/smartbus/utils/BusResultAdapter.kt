package com.ugdevs.smartbus.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ugdevs.smartbus.R

class BusResultAdapter: RecyclerView.Adapter<BusResultAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bus_result, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val busNumber = itemView.findViewById<TextView>(R.id.bus_number)
        val busETA = itemView.findViewById<TextView>(R.id.bus_eta)
        val busPrice = itemView.findViewById<TextView>(R.id.bus_price)
        val busPay = itemView.findViewById<ImageButton>(R.id.bus_pay)
    }
}