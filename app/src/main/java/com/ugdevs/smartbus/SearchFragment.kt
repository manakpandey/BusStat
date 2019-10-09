package com.ugdevs.smartbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ugdevs.smartbus.utils.NearbyStopsAdapter

class SearchFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nearbyStopsList = view.findViewById<RecyclerView>(R.id.nearby_stop_list)
        nearbyStopsList.adapter = NearbyStopsAdapter()

        val searchSource = view.findViewById<EditText>(R.id.search_source)
        val searchDestination = view.findViewById<EditText>(R.id.dest_search)
        val searchButton = view.findViewById<FloatingActionButton>(R.id.search_button)

        searchButton.setOnClickListener {

        }
    }
}