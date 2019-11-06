package com.ugdevs.smartbus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ugdevs.smartbus.utils.BusResultAdapter
import com.ugdevs.smartbus.utils.MainViewModel
import kotlinx.android.synthetic.main.fragment_search_results.*
import kotlin.collections.HashMap
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class SearchResultsFragment: Fragment() {

    private lateinit var model: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchResultList = view.findViewById<RecyclerView>(R.id.search_result_list)
        val busResultAdapter = BusResultAdapter()

        getResults()

        searchResultList.layoutManager = LinearLayoutManager(view.context)
        busResultAdapter.setModel(model)
        searchResultList.adapter = busResultAdapter

        tv_no_results.visibility = View.GONE
        tv_no_network.visibility = View.GONE
        searchResultList.visibility = View.VISIBLE
        loading_results.visibility = View.VISIBLE

        model.busResults.observe(this, Observer<List<HashMap<String,String>>>{
            busResultAdapter.setResults(it)
            loading_results.visibility = View.GONE
            if (it.isEmpty()){
                searchResultList.visibility = View.GONE
                tv_no_results.visibility = View.VISIBLE
            }

        })
    }

    private fun getResults(){
        val src = model.searchSrc
        val dest = model.searchDest
        val res= mutableListOf<HashMap<String,String>>()

        val queue = Volley.newRequestQueue(activity)
        var url = "http://192.168.43.21:8000/bus/search/src=$src&dest=$dest/"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
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
            Response.ErrorListener { error ->
                Log.d("SearchFragmentError", "Error: $error")
                tv_no_network.visibility = View.VISIBLE
                loading_results.visibility = View.GONE
            }
        )

        queue.add(jsonArrayRequest)
        url = "http://192.168.43.21:8000/stops/$src/"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val lat = response.getString("lat")
                val lon = response.getString("lon")
                model.busResults.value = res
                for (i in res){
                    i["eta"] = calculateEta(lat,lon,i["curr_lat"],i["curr_lon"],i["avg_speed"])
                    model.busResults.value = res

                }
            },
            Response.ErrorListener { error ->
                Log.d("SearchFragmentError", "Error: $error")
                tv_no_network.visibility = View.VISIBLE
                loading_results.visibility = View.GONE
            })
        queue.add(jsonObjectRequest)
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