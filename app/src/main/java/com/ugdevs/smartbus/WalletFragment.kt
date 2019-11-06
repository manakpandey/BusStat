package com.ugdevs.smartbus

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_display.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import org.jetbrains.anko.toast
import org.json.JSONObject


class WalletFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = activity?.getSharedPreferences(activity?.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        wallet_balance.text = sharedPref?.getInt("wallet_balance", 0).toString()

        val queue = Volley.newRequestQueue(activity)
        val url = "http://192.168.43.21:8000/user/verify/"

        val username = sharedPref?.getString("username","")
        val pwd = sharedPref?.getString("password","")
        val data = HashMap<String,String>()
        data["id"] = username!!
        data["password"] = pwd!!

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, JSONObject(data),
            Response.Listener {
                if (!it.has("access")){
                    with(sharedPref.edit()){
                        putInt("wallet_balance", it.getString("wallet_balance").toInt())
                        apply()
                    }
                }
            },
            Response.ErrorListener {
                Log.d("DisplayTokenFragment", "Error: $it")
                activity?.toast("Cannot refresh wallet balance.")
            })
        queue.add(jsonObjectRequest)
    }
}