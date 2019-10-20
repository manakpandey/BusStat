package com.ugdevs.smartbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.ugdevs.smartbus.utils.BusResultAdapter
import com.ugdevs.smartbus.utils.MainViewModel
import com.ugdevs.smartbus.utils.NearbyStopsAdapter
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment: Fragment() {

    private lateinit var model: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchResultList = view.findViewById<RecyclerView>(R.id.search_result_list)
        val nearbyStopsAdapter = NearbyStopsAdapter()
        val busResultAdapter = BusResultAdapter()

        searchResultList.layoutManager = LinearLayoutManager(view.context)
        searchResultList.adapter = nearbyStopsAdapter

        val test = listOf(listOf("VIT Bus Stop", "0.2mi"), listOf("Kelambakkam","7.0mi"))
        nearbyStopsAdapter.setResults(test)

        val searchSource: TextInputEditText = view.findViewById(R.id.search_source)
        val searchDestination: TextInputEditText = view.findViewById(R.id.search_destination)
        val searchButton = view.findViewById<FloatingActionButton>(R.id.search_button)

        searchButton.setOnClickListener {
            if (!(searchSource.text!!.isBlank() || searchDestination.text!!.isBlank())){
                busResultAdapter.setModel(model)
                searchResultList.adapter = busResultAdapter
                busResultAdapter.setResults(getResults(searchSource.text.toString(),searchDestination.text.toString()))
                results_heading.text = getString(R.string.bus_results)
            }
        }
    }

    private fun getResults(src: String, dest: String): List<HashMap<String,String>>{
        val v = HashMap<String,String>()
        val v1 = HashMap<String,String>()
        val res= mutableListOf<HashMap<String,String>>()
        v["number"]="515H"
        v["eta"]="5 Min"
        v["vac"]="20"
        v["price"]="15"
        res.add(v)
        v1["number"]="221B"
        v1["eta"]="8 Min"
        v1["vac"]="2"
        v1["price"]="23"
        res.add(v1)
        return res
    }
}