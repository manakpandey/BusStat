package com.ugdevs.smartbus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.ugdevs.smartbus.utils.BusResultAdapter
import com.ugdevs.smartbus.utils.MainViewModel
import com.ugdevs.smartbus.utils.NearbyStopsAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

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

//        val test = listOf(listOf("VIT Bus Stop", "0.2mi"), listOf("Kelambakkam","7.0mi"))
//        nearbyStopsAdapter.setResults(test)

        val searchSource: TextInputEditText = view.findViewById(R.id.search_source)
        val searchDestination: TextInputEditText = view.findViewById(R.id.search_destination)
        val searchButton = view.findViewById<FloatingActionButton>(R.id.search_button)

        searchButton.setOnClickListener {
            if (!(searchSource.text!!.isBlank() || searchDestination.text!!.isBlank())){
                busResultAdapter.setModel(model)
                searchResultList.adapter = busResultAdapter
                busResultAdapter.setResults(
                    getResults(searchSource.text.toString().toLowerCase(Locale.ENGLISH),
                                searchDestination.text.toString().toLowerCase(Locale.ENGLISH)))
                results_heading.text = getString(R.string.bus_results)
            }
        }
    }

    private fun getResults(src: String, dest: String): List<HashMap<String,String>>{
        val res= mutableListOf<HashMap<String,String>>()

        val queue = Volley.newRequestQueue(activity)
        var url = "http://192.168.43.21:8000/bus/search/src=$src&dest=$dest/"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    for (i in 0 until response.length()){
                        val data = HashMap<String,String>()
                        data["number"] = response.getJSONObject(i).getString("number")
                        data["price"] = response.getJSONObject(i)["price"].toString()
                        data["vac"] = (response.getJSONObject(i)["capacity"].toString().toInt()-
                                response.getJSONObject(i)["occupied"].toString().toInt()).toString()
                        data["avg_speed"] = response.getJSONObject(i).getString("avg_speed")
                        data["curr_lat"] = response.getJSONObject(i).getString("curr_lat")
                        data["curr_lon"] = response.getJSONObject(i).getString("curr_lon")

                        res.add(data)
                    }
                },
                Response.ErrorListener {error ->
                    Log.d("SearchFragmentError", "Error: $error")
                }
            )

        queue.add(jsonArrayRequest)
        url = "http://192.168.43.21:8000/stops/$src/"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val lat = response.getString("lat")
                val lon = response.getString("lon")

                for (i in res){
                    i["eta"] = calculateEta(lat,lon,i["curr_lat"],i["curr_lon"],i["avg_speed"])
                }

            },
            Response.ErrorListener { error ->
                Log.d("SearchFragmentError", "Error: $error")
            })
        queue.add(jsonObjectRequest)

        return res
    }

    private fun calculateEta(
        lat: String?,
        lon: String?,
        lat_bus: String?,
        lon_bus: String?,
        speed: String?
    ): String {
        val theta = lon?.toDouble()?.minus(lon_bus?.toDouble()!!)
        var dist = sin(deg2rad(lat?.toDouble())) * sin(deg2rad(lat_bus?.toDouble())) + (cos(deg2rad(lat?.toDouble()))
                * cos(deg2rad(lat_bus?.toDouble()))
                * cos(deg2rad(theta)))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515

        val eta = dist/ speed?.toDouble()!!
        return (eta*60).roundToInt().toString()+" Min"
    }

    private fun deg2rad(deg: Double?) = deg!! * Math.PI / 180.0
    private fun rad2deg(rad: Double?) = rad!! * 180.0 / Math.PI
}