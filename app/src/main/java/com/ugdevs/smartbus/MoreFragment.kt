package com.ugdevs.smartbus

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_more.*
import org.jetbrains.anko.startActivity

class MoreFragment :Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getSharedPreferences(activity?.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username","")
        val userText = "Hey, $username"
        more_username.text = userText
        val logoutText = "Not $username, logout?"
        logout_button.text = logoutText
        logout_button.setOnClickListener {

            with(sharedPref?.edit()){
                this?.putBoolean("logged_in", false)
                this?.apply()
            }

            activity?.startActivity<LoginActivity>()

        }
    }


}