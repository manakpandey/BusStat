package com.ugdevs.smartbus.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ugdevs.smartbus.R

class BusResultAdapter: RecyclerView.Adapter<BusResultAdapter.ViewHolder>() {

    private var results = emptyList<HashMap<String,String>>()
    private var model: MainViewModel = MainViewModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bus_result, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.busNumber.text = results[position]["number"]
        holder.busETA.text = results[position]["eta"]
        holder.busPrice.text = results[position]["price"]
        holder.busVac.text = results[position]["vac"]

        holder.busResultCard.setOnClickListener {view->

            model.selectedBus = results[position]
            view.findNavController().navigate(R.id.dest_displayFragment)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val busNumber: TextView = itemView.findViewById(R.id.bus_number)
        val busETA: TextView = itemView.findViewById(R.id.bus_eta)
        val busPrice: TextView = itemView.findViewById(R.id.bus_price)
        val busVac: TextView = itemView.findViewById(R.id.bus_vac_seats)
        val busResultCard: CardView = itemView.findViewById(R.id.bus_result_card)
    }

    fun setResults(results: List<HashMap<String,String>>){
        this.results = results
        notifyDataSetChanged()
    }
    fun setModel(model: MainViewModel){
        this.model = model
    }
}