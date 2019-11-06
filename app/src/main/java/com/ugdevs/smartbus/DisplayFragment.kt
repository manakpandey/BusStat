package com.ugdevs.smartbus

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ugdevs.smartbus.utils.MainViewModel
import kotlinx.android.synthetic.main.fragment_display.*
import org.jetbrains.anko.toast
import org.json.JSONObject

class DisplayFragment : Fragment(){

    private lateinit var model: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        display_bus_number.text = model.selectedBus["number"]
        display_mode.text  = model.selectedBus["mode"]
        display_amt.text = model.selectedBus["price"]

        getTokenFromServer()
    }

    private fun getTokenFromServer() {

        val sharedPref: SharedPreferences = activity?.getSharedPreferences((activity?.getString(R.string.preference_file_key)), Context.MODE_PRIVATE)!!

        val username = sharedPref.getString("username","")
        val pwd = sharedPref.getString("password","")

        val queue = Volley.newRequestQueue(activity)
        val url1 = "http://192.168.43.21:8000/user/verify/"
        val url2 = "http://192.168.43.21:8000/token/generate/"
        val data = HashMap<String,String>()
        data["id"] = username!!
        data["password"] = pwd!!

        val jsonObjectRequest1 = JsonObjectRequest(Request.Method.POST, url1, JSONObject(data),
            Response.Listener {
                if (!it.has("access")){
                    with(sharedPref.edit()){
                        putInt("wallet_balance", it.getString("wallet_balance").toInt())
                        apply()
                    }
                }
            },
            Response.ErrorListener {
                Log.d("DisplayFragment", "Error: $it")
            })
        queue.add(jsonObjectRequest1)

        if(sharedPref.getInt("wallet_balance",0) >= model.selectedBus["price"]?.toInt()!!){
            val data1 = HashMap<String,String>()
            data1["username"] = username
            data1["amount"] = model.selectedBus["price"]!!

            val jsonObjectRequest2 = JsonObjectRequest(Request.Method.POST, url2, JSONObject(data1),
                Response.Listener {
                    display_code.text = it.getString("id")
                },
                Response.ErrorListener {
                    Log.d("DisplayFragment", "Error: $it")
                })
            queue.add(jsonObjectRequest2)
        }
        else{
            activity!!.toast("Something went wrong!")
        }
    }

}