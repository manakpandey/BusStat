package com.ugdevs.smartbus

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        if (sharedPref.getBoolean("logged_in", false)){
            startActivity<MainActivity>()
        }

        login_button.setOnClickListener {

            if (!(login_username.text!!.isBlank() || login_password.text!!.isBlank())){
                login_progress.visibility = View.VISIBLE
                verifyUser(login_username.text.toString(),login_password.text.toString(), sharedPref)
            }
            else {
                toast("Please enter credentials")
            }
        }
    }


    private fun verifyUser(username: String, password: String, sharedPref: SharedPreferences){

        val data = HashMap<String,String>()
        data["id"] = username
        data["password"] = password

        val url = "http://192.168.43.21:8000/user/verify/"

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, JSONObject(data),
            Response.Listener {
                if (!it.has("access")){
                    with(sharedPref.edit()){
                        putString("username",it.getString("id"))
                        putString("password",it.getString("password"))
                        putInt("wallet_balance", it.getInt("wallet_balance"))
                        putBoolean("logged_in", true)
                        apply()
                    }
                    startActivity<MainActivity>()
                }
                else{
                    login_progress.visibility = View.GONE
                    toast("Invalid Credentials")
                }
            },
            Response.ErrorListener {
                Log.d("LoginActivity", "Error: $it")
                login_progress.visibility = View.GONE
                toast("Login Failed")
            })

        queue.add(jsonObjectRequest)
    }
}
