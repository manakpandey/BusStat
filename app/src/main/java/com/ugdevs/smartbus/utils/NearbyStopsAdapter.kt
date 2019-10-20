package com.ugdevs.smartbus.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ugdevs.smartbus.R

class NearbyStopsAdapter: RecyclerView.Adapter<NearbyStopsAdapter.ViewHolder>() {

    private var results = emptyList<List<String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nearby_stop_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.stopName.text = results[position][0]
            holder.stopDistance.text = results[position][1]
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val stopName: TextView = itemView.findViewById(R.id.stop_name)
        val stopDistance: TextView = itemView.findViewById(R.id.stop_distance)
    }

    fun setResults(results: List<List<String>>){
        this.results = results
        notifyDataSetChanged()
    }
}